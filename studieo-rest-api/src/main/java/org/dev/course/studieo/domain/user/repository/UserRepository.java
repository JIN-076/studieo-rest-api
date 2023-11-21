package org.dev.course.studieo.domain.user.repository;

import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findByUserId(UUID userId);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(Email email);
    User update(User user);
    void delete(UUID userId);
    void deleteAll();
}
