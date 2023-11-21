package org.dev.course.studieo.domain.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.Email;
import org.dev.course.studieo.domain.user.entity.vo.Tel;
import org.dev.course.studieo.util.converter.UUIDConverter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            UUID userId = UUIDConverter.toUUID(rs.getBytes("user_id"));
            String loginId = rs.getString("login_id");
            String name = rs.getString("name");
            Email email = Email.valueOf(rs.getString("email"));
            Integer age = Integer.parseInt(rs.getString("age"));
            String nickname = rs.getString("nickname");
            String password = rs.getString("password_hash");
            Tel tel = Tel.valueOf(rs.getString("tel"));
            Boolean sex = rs.getBoolean("sex");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(rs.getString("birth_date"), formatter);
            return new User(userId, loginId, name, email, age, nickname, password, tel, sex, birthDate);
        };
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO user (user_id, login_id, name, email, age, nickname, password_hash, tel, sex, birth_date)" +
                " VALUES(UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                UUIDConverter.toBytes(user.getUserId()),
                user.getLoginId(), user.getName(), user.getEmail().getEmail(), user.getAge(),
                user.getNickname(), user.getPassword(), user.getTel().getTel(), user.getSex(),
                user.getBirthDate());
        return user;
    }

    @Override
    public Optional<User> findByUserId(UUID userId) {
        String sql = "SELECT * FROM user WHERE user_id = UUID_TO_BIN(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper(),
                    (Object) UUIDConverter.toBytes(userId)));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found for userId: {}", userId);
        } catch (DataAccessException e) {
            log.error("Data access exception: {}", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        String sql = "SELECT * FROM user WHERE login_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper(), loginId));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found for loginId: {}", loginId);
        } catch (DataAccessException e) {
            log.error("Data access exception: {}", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        String sql = "SELECT * FROM user WHERE nickname = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper(), nickname));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found for nickname: '{}'", nickname);
        } catch (DataAccessException e) {
            log.error("Data access exception: {}", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper(), email.getEmail()));
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found for email: '{}'", email.getEmail());
        } catch (DataAccessException e) {
            log.error("Data access exception: {}", e.toString());
        }
        return Optional.empty();
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE user SET login_id = ?, name = ?, email = ?, age = ?, nickname = ?, password_hash = ?, tel = ?, sex = ?, birth_date = ?" +
                "WHERE user_id = UUID_TO_BIN(?)";
        jdbcTemplate.update(sql,
                user.getLoginId(), user.getName(), user.getEmail().getEmail(), user.getAge(),
                user.getNickname(), user.getPassword(), user.getTel().getTel(), user.getSex(), user.getBirthDate(),
                UUIDConverter.toBytes(user.getUserId()));
        return user;
    }

    @Override
    public void delete(UUID userId) {

    }

    @Override
    public void deleteAll() {

    }
}
