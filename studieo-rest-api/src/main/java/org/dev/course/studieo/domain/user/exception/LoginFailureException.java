package org.dev.course.studieo.domain.user.exception;

public class LoginFailureException extends RuntimeException {

    public LoginFailureException() {
        super("Login failed! Please check your Id, Passwd again.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
