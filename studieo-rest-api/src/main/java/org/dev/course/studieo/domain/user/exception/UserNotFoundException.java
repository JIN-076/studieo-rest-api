package org.dev.course.studieo.domain.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("No matching user found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
