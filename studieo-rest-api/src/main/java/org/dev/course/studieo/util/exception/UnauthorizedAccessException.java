package org.dev.course.studieo.util.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("You do not have permission to send notifications!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
