package org.dev.course.studieo.domain.study.exception;

public class StudyNotFoundException extends RuntimeException {

    public StudyNotFoundException() {
        super("No matching studies found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
