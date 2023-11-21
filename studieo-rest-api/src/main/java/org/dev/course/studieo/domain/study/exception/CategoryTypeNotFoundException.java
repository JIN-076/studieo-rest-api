package org.dev.course.studieo.domain.study.exception;

public class CategoryTypeNotFoundException extends RuntimeException {

    public CategoryTypeNotFoundException() {
        super("No matching categoryType found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
