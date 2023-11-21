package org.dev.course.studieo.domain.participant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.participant.dto.ParticipantDto;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.entity.RoleType;
import org.dev.course.studieo.domain.participant.exception.ParticipantNotFoundException;
import org.dev.course.studieo.domain.participant.exception.TeamLeaderNotFoundException;
import org.dev.course.studieo.domain.participant.mapper.ParticipantMapper;
import org.dev.course.studieo.domain.participant.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;


    public Participant createStudy(ParticipantDto participantDto) {
        Participant participant = ParticipantMapper.INSTANCE.mapToEntity(participantDto, RoleType.TEAM_LEADER);
        return participantRepository.save(participant);
    }

    public Participant participateStudy(ParticipantDto participantDto) {
        Participant participant = ParticipantMapper.INSTANCE.mapToEntity(participantDto, participantDto.role());
        return participantRepository.save(participant);
    }

    public Participant acceptParticipation(Participant participant) {
        return participantRepository.update(participant);
    }

    public Participant findById(ParticipantDto participantDto) {
        return participantRepository.findById(participantDto.studyId(), participantDto.userId())
                .orElseThrow(() -> {
                    log.warn("No participant found for studyId '{}' and userId '{}'",
                            participantDto.studyId(), participantDto.userId());
                    throw new ParticipantNotFoundException();
                });
    }

    public List<Participant> findByLeader(ParticipantDto participantDto) {
        return participantRepository.findByLeader(participantDto.userId());
    }

    public Participant findLeaderById(ParticipantDto participantDto) {
        return participantRepository.findLeaderById(participantDto.studyId())
                .orElseThrow(() -> {
                    log.warn("No leader found for studyId '{}'", participantDto.studyId());
                    throw new TeamLeaderNotFoundException();
                });
    }

    public boolean isExist(ParticipantDto participantDto) {
        return participantRepository.findById(participantDto.studyId(), participantDto.userId()).isPresent();
    }

    public RoleType getRole(ParticipantDto participantDto) {
        try {
            Participant participant = findById(participantDto);
            return participant.getRole();
        } catch (ParticipantNotFoundException e) {
            return RoleType.NONE;
        }
    }
}
