package org.dev.course.studieo.domain.participant.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.entity.RoleType;
import org.dev.course.studieo.util.converter.UUIDConverter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcParticipantRepository implements ParticipantRepository {

    private final JdbcTemplate jdbcTemplate;

    private static RowMapper<Participant> participantRowMapper() {
        return (rs, rowNum) -> {
            UUID studyId = UUIDConverter.toUUID(rs.getBytes("study_id"));
            UUID userId = UUIDConverter.toUUID(rs.getBytes("user_id"));
            RoleType role = RoleType.valueOf(rs.getInt("role"));
            return new Participant(studyId, userId, role);
        };
    }

    @Override
    public Participant save(Participant participant) {
        String sql = "INSERT INTO participant (study_id, user_id, role)" +
                "VALUES(UUID_TO_BIN(?), UUID_TO_BIN(?), ?)";
        jdbcTemplate.update(sql,
                UUIDConverter.toBytes(participant.getStudyId()),
                UUIDConverter.toBytes(participant.getUserId()),
                participant.getRole().getValue());
        return participant;
    }

    @Override
    public Participant update(Participant participant) {
        String sql = "UPDATE participant SET role = ? WHERE study_id=UUID_TO_BIN(?) AND user_id=UUID_TO_BIN(?)";
        jdbcTemplate.update(sql,
                participant.getRole().getValue(),
                UUIDConverter.toBytes(participant.getStudyId()),
                UUIDConverter.toBytes(participant.getUserId()));
        return participant;
    }

    @Override
    public Optional<Participant> findById(UUID studyId, UUID userId) {
        String sql = "SELECT * FROM participant WHERE study_id=UUID_TO_BIN(?) AND user_id=UUID_TO_BIN(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, participantRowMapper(),
                    UUIDConverter.toBytes(studyId), UUIDConverter.toBytes(userId)));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No participant for studyId '{}' and userId '{}'", studyId, userId);
        } catch (DataAccessException e) {
            log.warn("Data access exception '{}'", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public List<Participant> findByLeader(UUID userId) {
        String sql = "SELECT * FROM participant WHERE user_id=UUID_TO_BIN(?) AND role=1";
        return jdbcTemplate.query(sql, participantRowMapper(), (Object) UUIDConverter.toBytes(userId));
    }

    @Override
    public Optional<Participant> findLeaderById(UUID studyId) {
        String sql = "SELECT * FROM participant WHERE study_id=UUID_TO_BIN(?) AND role=1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, participantRowMapper(),
                    (Object) UUIDConverter.toBytes(studyId)));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No teamLeader for studyId '{}'", studyId);
        } catch (DataAccessException e) {
            log.warn("Data access exception '{}'", e.toString());
        }
        return Optional.empty();
    }
}
