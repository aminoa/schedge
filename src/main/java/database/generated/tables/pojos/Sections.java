/*
 * This file is generated by jOOQ.
 */
package database.generated.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sections implements Serializable {

    private static final long serialVersionUID = 59121969;

    private final Integer id;
    private final Integer registrationNumber;
    private final Integer courseId;
    private final String  sectionCode;
    private final String  instructor;
    private final Integer sectionType;
    private final Integer sectionStatus;
    private final Integer associatedWith;

    public Sections(Sections value) {
        this.id = value.id;
        this.registrationNumber = value.registrationNumber;
        this.courseId = value.courseId;
        this.sectionCode = value.sectionCode;
        this.instructor = value.instructor;
        this.sectionType = value.sectionType;
        this.sectionStatus = value.sectionStatus;
        this.associatedWith = value.associatedWith;
    }

    public Sections(
        Integer id,
        Integer registrationNumber,
        Integer courseId,
        String  sectionCode,
        String  instructor,
        Integer sectionType,
        Integer sectionStatus,
        Integer associatedWith
    ) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.courseId = courseId;
        this.sectionCode = sectionCode;
        this.instructor = instructor;
        this.sectionType = sectionType;
        this.sectionStatus = sectionStatus;
        this.associatedWith = associatedWith;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public String getSectionCode() {
        return this.sectionCode;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public Integer getSectionType() {
        return this.sectionType;
    }

    public Integer getSectionStatus() {
        return this.sectionStatus;
    }

    public Integer getAssociatedWith() {
        return this.associatedWith;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Sections (");

        sb.append(id);
        sb.append(", ").append(registrationNumber);
        sb.append(", ").append(courseId);
        sb.append(", ").append(sectionCode);
        sb.append(", ").append(instructor);
        sb.append(", ").append(sectionType);
        sb.append(", ").append(sectionStatus);
        sb.append(", ").append(associatedWith);

        sb.append(")");
        return sb.toString();
    }
}
