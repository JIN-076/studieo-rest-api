package org.dev.course.studieo.domain.participant.exception;

public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException() {
        super("No matching participant found!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
