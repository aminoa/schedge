/*
 * This file is generated by jOOQ.
 */
package database.generated;


import database.generated.tables.Courses;
import database.generated.tables.Epochs;
import database.generated.tables.FlywaySchemaHistory;
import database.generated.tables.Meetings;
import database.generated.tables.Sections;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in 
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>courses</code>.
     */
    public static final Courses COURSES = Courses.COURSES;

    /**
     * The table <code>epochs</code>.
     */
    public static final Epochs EPOCHS = Epochs.EPOCHS;

    /**
     * The table <code>flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>meetings</code>.
     */
    public static final Meetings MEETINGS = Meetings.MEETINGS;

    /**
     * The table <code>sections</code>.
     */
    public static final Sections SECTIONS = Sections.SECTIONS;
}
