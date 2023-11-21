package org.dev.course.studieo.domain.notification.mapper;

import org.dev.course.studieo.domain.notification.entity.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "receiverId", source = "receiverId")
    NotificationDto mapToDto(UUID receiverId);

    @Mapping(target = "studyId", source = "studyId")
    @Mapping(target = "senderId", source = "senderId")
    NotificationDto mapToDtoWithId(UUID studyId, UUID senderId);
}
