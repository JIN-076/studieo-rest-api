package org.dev.course.studieo.domain.study.repository;

import org.dev.course.studieo.domain.study.entity.Study;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudyRepository {

    Study save(Study studies);
    Optional<Study> findById(UUID studiesId);
    List<Study> findAll();
}
