package org.dev.course.studieo.domain.study.mapper;

import org.dev.course.studieo.domain.study.dto.StudyDto;
import org.dev.course.studieo.domain.study.dto.StudySaveRequestDto;
import org.dev.course.studieo.domain.study.entity.CategoryType;
import org.dev.course.studieo.domain.study.entity.Study;
import org.dev.course.studieo.domain.study.entity.StudyType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudyMapper {

    StudyMapper INSTANCE = Mappers.getMapper(StudyMapper.class);


    @Mapping(target = "studyType", expression = "java(mapToStudiesType(studySaveRequestDto.getStudyType()))")
    @Mapping(target = "category", expression = "java(mapToCategoryType(studySaveRequestDto.getCategory()))")
    @Mapping(target = "address", expression = "java(studySaveRequestDto.getAddress())")
    @Mapping(target = "latitude", source = "y", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "longitude", source = "x", nullValuePropertyMappingStrategy = IGNORE)
    StudyDto mapToDto(StudySaveRequestDto studySaveRequestDto, Double x, Double y);

    @Mapping(target = "studyId", source = "studyId")
    @Mapping(target = "headCount", source = "headCount")
    @Mapping(target = "address", expression = "java(studyDto.address())")
    Study mapToEntity(StudyDto studyDto, UUID studyId, int headCount);

    default CategoryType mapToCategoryType(String value) {
        return CategoryType.valueOf(value);
    }

    default StudyType mapToStudiesType(String value) {
        return StudyType.valueOf(value);
    }
}
