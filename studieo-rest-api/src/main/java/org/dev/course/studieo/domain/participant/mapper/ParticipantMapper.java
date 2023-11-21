package org.dev.course.studieo.domain.participant.mapper;

import org.dev.course.studieo.domain.participant.dto.ParticipantDto;
import org.dev.course.studieo.domain.participant.entity.Participant;
import org.dev.course.studieo.domain.participant.entity.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    @Mapping(target = "studyId", source = "studyId")
    @Mapping(target = "userId", source = "userId")
    ParticipantDto mapToDto(UUID studyId, UUID userId);

    @Mapping(target = "studyId", source = "studyId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "role", source = "role")
    ParticipantDto mapToDtoWithRole(UUID studyId, UUID userId, RoleType role);

    @Mapping(target = "userId", source = "userId")
    ParticipantDto mapToDto(UUID userId);

    @Mapping(target = "studyId", source = "studyId")
    ParticipantDto mapToDtoWithStudyId(UUID studyId);

    @Mapping(target = "role", source = "role")
    Participant mapToEntity(ParticipantDto participantDto, RoleType role);
}
