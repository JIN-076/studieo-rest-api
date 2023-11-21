package org.dev.course.studieo.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.notification.entity.Notification;
import org.dev.course.studieo.util.converter.UUIDConverter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcNotificationRepository implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    private static RowMapper<Notification> notificationRowMapper() {
        return (rs, rowNum) -> {
            UUID studyId = UUIDConverter.toUUID(rs.getBytes("study_id"));
            UUID receiverId = UUIDConverter.toUUID(rs.getBytes("receiver_id"));
            UUID senderId = UUIDConverter.toUUID(rs.getBytes("sender_id"));
            String content = rs.getString("content");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"), formatter);
            boolean isRead = rs.getBoolean("is_read");
            return new Notification(studyId, receiverId, senderId, content, createdAt, isRead);
        };
    }

    @Override
    public Notification save(Notification notification) {
        String sql = "INSERT INTO notification (study_id, receiver_id, sender_id, content, created_at, is_read)" +
                "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?)";
        jdbcTemplate.update(sql,
                UUIDConverter.toBytes(notification.getStudyId()),
                UUIDConverter.toBytes(notification.getReceiverId()),
                UUIDConverter.toBytes(notification.getSenderId()),
                notification.getContent(),
                notification.getCreatedAt(),
                notification.getIsRead());
        return notification;
    }

    @Override
    public Optional<Notification> findById(UUID studyId, UUID receiverId, UUID senderId) {
        String sql = "SELECT * FROM notification WHERE study_id=UUID_TO_BIN(?) AND receiver_id=UUID_TO_BIN(?) AND sender_id=UUID_TO_BIN(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, notificationRowMapper(),
                    UUIDConverter.toBytes(receiverId),
                    UUIDConverter.toBytes(senderId)));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No notification found for studyId '{}', receiverId '{}', senderId '{}'",
                    studyId.toString(), receiverId.toString(), senderId.toString());
        } catch (DataAccessException e) {
            log.error("Data access Exception '{}'", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public List<Notification> findByStudyId(UUID studyId) {
        String sql = "SELECT * FROM notification WHERE study_id=UUID_TO_BIN(?)";
        return jdbcTemplate.query(sql, notificationRowMapper(), (Object) UUIDConverter.toBytes(studyId));
    }

    @Override
    public List<Notification> findByReceiverId(UUID receiverId) {
        String sql = "SELECT * FROM notification WHERE receiver_id=UUID_TO_BIN(?)";
        return jdbcTemplate.query(sql, notificationRowMapper(), (Object) UUIDConverter.toBytes(receiverId));
    }

    @Override
    public void delete(UUID studyId, UUID senderId) {
        String sql = "DELETE FROM notification WHERE study_id=UUID_TO_BIN(?) AND sender_id=UUID_TO_BIN(?)";
        jdbcTemplate.update(sql, UUIDConverter.toBytes(studyId), UUIDConverter.toBytes(senderId));
    }
}
