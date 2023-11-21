package org.dev.course.studieo.domain.participant.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("No matching role found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
