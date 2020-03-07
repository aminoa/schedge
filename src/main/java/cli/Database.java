package cli;

import api.App;
import cli.templates.OutputFileMixin;
import cli.templates.SubjectCodeMixin;
import cli.templates.TermMixin;
import database.*;
import database.courses.InsertCourses;
import database.courses.SelectCourses;
import database.courses.UpdateSections;
import database.epochs.CleanEpoch;
import database.epochs.CompleteEpoch;
import database.epochs.GetNewEpoch;
import database.epochs.LatestCompleteEpoch;
import database.instructors.UpdateInstructors;
import database.models.SectionID;
import java.util.Iterator;
import java.util.List;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import nyu.SubjectCode;
import nyu.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import scraping.ScrapeCatalog;

@CommandLine.Command(name = "db", synopsisSubcommandLabel =
                                      "(scrape | query | update | serve)")
public class Database implements Runnable {
  @CommandLine.Spec private CommandLine.Model.CommandSpec spec;

  private static Logger logger = LoggerFactory.getLogger("cli.Database");

  @Override
  public void run() {
    throw new CommandLine.ParameterException(spec.commandLine(),
                                             "Missing required subcommand");
  }

  @CommandLine.Command(
      name = "scrape", sortOptions = false, headerHeading = "Usage:%n%n",
      synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
      parameterListHeading = "%nParameters:%n",
      optionListHeading = "%nOptions:%n", header = "Scrape section from db",
      description =
          "Scrape section based on term and registration number, OR school and subject from db")
  public void
  scrape(@CommandLine.Mixin TermMixin termMixin,
         @CommandLine.
         Option(names = "--batch-size-catalog",
                description = "batch size for querying the catalog")
         Integer batchSize,
         @CommandLine.Option(names = "--batch-size-sections",
                             description = "batch size for querying sections")
         Integer batchSizeSections) {
    Term term = termMixin.getTerm();
    long start = System.nanoTime();
    GetConnection.withContext(context -> {
      int epoch = GetNewEpoch.getNewEpoch(context, term);
      List<SubjectCode> allSubjects = SubjectCode.allSubjects();
      ProgressBarBuilder barBuilder =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.ASCII)
              .setConsumer(new ConsoleProgressBarConsumer(System.out));
      Iterator<SectionID> s =
          ScrapeCatalog
              .scrapeFromCatalog(
                  term, ProgressBar.wrap(allSubjects, barBuilder), batchSize)
              .flatMap(courseList
                       -> InsertCourses
                              .insertCourses(context, term, epoch, courseList)
                              .stream())
              .iterator();
      UpdateSections.updateSections(context, term, s, batchSizeSections);

      CompleteEpoch.completeEpoch(context, term, epoch);
    }

    );

    long end = System.nanoTime();
    logger.info((end - start) / 1000000000 + " seconds");
  }

  @CommandLine.
  Command(name = "rmp", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Scrape section from db",
          description = "Update instructors using RMP")
  public void
  rmp(@CommandLine.Option(names = "--batch-size",
                          description = "batch size for querying RMP")
      Integer batchSize) {
    long start = System.nanoTime();
    GetConnection.withContext(context -> {
      List<SubjectCode> allSubjects = SubjectCode.allSubjects();
      ProgressBarBuilder barBuilder =
          new ProgressBarBuilder()
              .setStyle(ProgressBarStyle.ASCII)
              .setConsumer(new ConsoleProgressBarConsumer(System.out));
      UpdateInstructors.updateInstructors(
          context,
          ProgressBar.wrap(UpdateInstructors.instructorUpdateList(context),
                           barBuilder),
          batchSize);
    }

    );

    long end = System.nanoTime();
    logger.info((end - start) / 1000000000 + " seconds");
  }

  @CommandLine.Command(
      name = "query", sortOptions = false, headerHeading = "Usage:%n%n",
      synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
      parameterListHeading = "%nParameters:%n",
      optionListHeading = "%nOptions:%n", header = "Query section",
      description =
          "Query section based on term and registration number, OR school and subject from db")
  public void
  query(@CommandLine.Mixin TermMixin termMixin,
        @CommandLine.Mixin SubjectCodeMixin subjectCodeMixin,
        @CommandLine.Mixin OutputFileMixin outputFile) {
    long start = System.nanoTime();
    GetConnection.withContext(context -> {
      Term term = termMixin.getTerm();
      Integer epoch = LatestCompleteEpoch.getLatestEpoch(context, term);
      if (epoch == null) {
        logger.warn("No completed epoch for term=" + term);
        return;
      }
      outputFile.writeOutput(SelectCourses.selectCourses(
          context, epoch, subjectCodeMixin.getSubjectCodes()));
    });

    long end = System.nanoTime();
    double duration = (end - start) / 1000000000.0;
    logger.info(duration + " seconds");
  }

  @CommandLine.
  Command(name = "clean", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Serve data",
          description = "Clean epochs")
  public void
  clean(@CommandLine.Mixin TermMixin termMixin,
        @CommandLine.Option(names = "--epoch",
                            description = "The epoch to clean") Integer epoch) {
    Term term = termMixin.getTermAllowNull();
    if (epoch == null && term == null) {
      logger.info("Cleaning incomplete epochs...");
      CleanEpoch.cleanIncompleteEpochs();
    } else if (epoch != null && term == null) {
      logger.info("Cleaning epoch={}...", epoch);
      CleanEpoch.cleanEpoch(epoch);
    } else if (term != null && epoch == null) {
      logger.info("Cleaning epochs for term={}...", term);
      CleanEpoch.cleanEpoch(term);
    } else {
      System.err.println("Term and --epoch are mutually exclusive!");
      System.exit(1);
    }
  }

  @CommandLine.
  Command(name = "serve", sortOptions = false, headerHeading = "Usage:%n%n",
          synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n",
          parameterListHeading = "%nParameters:%n",
          optionListHeading = "%nOptions:%n", header = "Serve data",
          description = "Serve data through an API")
  public void
  serve() {
    App.run();
  }
}
