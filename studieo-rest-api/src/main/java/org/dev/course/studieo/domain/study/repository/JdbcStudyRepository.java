package org.dev.course.studieo.domain.study.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.Study;
import org.dev.course.studieo.domain.study.entity.StudyType;
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
public class JdbcStudyRepository implements StudyRepository {

    private final JdbcTemplate jdbcTemplate;

    private static RowMapper<Study> studiesRowMapper() {
        return (rs, rowNum) -> {
            UUID studyId = UUIDConverter.toUUID(rs.getBytes("study_id"));
            String name = rs.getString("name");
            StudyType studyType = StudyType.valueOf(rs.getInt("study_type"));
            String description = rs.getString("description");
            String subject = rs.getString("subject");
            CategoryType categoryType = CategoryType.valueOf(rs.getInt("category"));
            String requirement = rs.getString("requirement");
            String address = rs.getString("address");
            Double latitude = rs.getDouble("latitude");
            Double longitude = rs.getDouble("longitude");
            int capacity = rs.getInt("capacity");
            int headCount = rs.getInt("head_count");
            int times = rs.getInt("times");
            boolean isAccept = rs.getBoolean("is_accept");
            return new Study(studyId, name, studyType, description, subject, categoryType, requirement, address, latitude, longitude, capacity, headCount, times, isAccept);
        };
    }

    @Override
    public Study save(Study studies) {
        String sql = "INSERT INTO studies" +
                "(study_id, name, study_type, description, subject, category, requirement, address, latitude, longitude, capacity, head_count, times, is_accept)" +
                "VALUES(UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                UUIDConverter.toBytes(studies.getStudyId()),
                studies.getName(), studies.getStudyType().getValue(), studies.getDescription(), studies.getSubject(),
                studies.getCategory().getValue(), studies.getRequirement(), studies.getAddress(), studies.getLatitude(),
                studies.getLongitude(), studies.getCapacity(), studies.getHeadCount(), studies.getTimes(), studies.isAccept());
        return studies;
    }

    @Override
    public Optional<Study> findById(UUID studyId) {
        String sql = "SELECT * FROM studies WHERE study_id=UUID_TO_BIN(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, studiesRowMapper(),
                    (Object) UUIDConverter.toBytes(studyId)));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No studies found for studiesId: {}", studyId);
        } catch (DataAccessException e) {
            log.error("Data access exception: {}", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public List<Study> findAll() {
        String sql = "SELECT * from studies";
        return jdbcTemplate.query(sql, studiesRowMapper());
    }
}
