package org.dev.course.studieo.domain.study.exception;

public class StudyTypeNotFoundException extends RuntimeException {

    public StudyTypeNotFoundException() {
        super("No matching studiesType found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
