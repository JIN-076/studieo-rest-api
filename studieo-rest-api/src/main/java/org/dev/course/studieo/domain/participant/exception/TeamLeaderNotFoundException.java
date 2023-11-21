package org.dev.course.studieo.domain.participant.exception;

public class TeamLeaderNotFoundException extends RuntimeException {

    public TeamLeaderNotFoundException() {
        super("No found matching teamLeader!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
