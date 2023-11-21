package org.dev.course.studieo.domain.study.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dev.course.studieo.domain.study.dto.StudyDto;
import org.dev.course.studieo.domain.study.entity.Study;
import org.dev.course.studieo.domain.study.exception.StudyNotFoundException;
import org.dev.course.studieo.domain.study.mapper.StudyMapper;
import org.dev.course.studieo.domain.study.repository.StudyRepository;
import org.dev.course.studieo.util.generator.UUIDGenerator;
import org.mapstruct.Context;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final UUIDGenerator uuidGenerator;

    public Study save(StudyDto studyDto) {
        UUID studyId = generateUUID(uuidGenerator);
        Study studies = StudyMapper.INSTANCE.mapToEntity(studyDto, studyId, 1);
        return save(studies);
    }

    public Study findById(UUID studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> {
                    log.warn("No studies found for studyId '{}'", studyId);
                    throw new StudyNotFoundException();
                });
    }

    public List<Study> findAll() {
        return studyRepository.findAll();
    }

    private Study save(Study studies) {
        return studyRepository.save(studies);
    }

    private UUID generateUUID(@Context UUIDGenerator uuidGenerator) {
        return uuidGenerator.generate();
    }
}
