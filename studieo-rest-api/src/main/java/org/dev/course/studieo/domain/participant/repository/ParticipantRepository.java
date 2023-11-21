package org.dev.course.studieo.domain.participant.repository;

import org.dev.course.studieo.domain.participant.entity.Participant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository {

    Participant save(Participant participant);
    Participant update(Participant participant);
    Optional<Participant> findById(UUID studyId, UUID userId);
    List<Participant> findByLeader(UUID userId);
    Optional<Participant> findLeaderById(UUID studyId);
}
