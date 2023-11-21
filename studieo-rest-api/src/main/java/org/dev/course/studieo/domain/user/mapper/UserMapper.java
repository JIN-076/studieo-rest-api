package org.dev.course.studieo.domain.user.mapper;

import org.dev.course.studieo.domain.user.dto.UserDto;
import org.dev.course.studieo.domain.user.dto.UserEditRequestDto;
import org.dev.course.studieo.domain.user.dto.UserGetRequestDto;
import org.dev.course.studieo.domain.user.dto.UserSaveRequestDto;
import org.dev.course.studieo.domain.user.entity.User;
import org.dev.course.studieo.domain.user.entity.vo.Email;
import org.dev.course.studieo.domain.user.entity.vo.Tel;
import org.dev.course.studieo.util.converter.BCryptConverter;
import org.dev.course.studieo.util.generator.UUIDGenerator;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId", expression = "java(generateUUID(uuidGenerator))")
    @Mapping(target = "email", expression = "java(mapToEmail(saveRequestDto.getEmail()))")
    @Mapping(target = "password", expression = "java(mapToEncodePasswd(saveRequestDto, bCryptConverter))")
    @Mapping(target = "tel", expression = "java(mapToTel(saveRequestDto.getTel()))")
    UserDto mapToDto(UserSaveRequestDto saveRequestDto, @Context UUIDGenerator uuidGenerator, @Context BCryptConverter bCryptConverter);

    @Mapping(target = "email", expression = "java(mapToEmail(user.getEmail()))")
    @Mapping(target = "tel", expression = "java(mapToTel(user.getTel()))")
    UserEditRequestDto mapToEditDto(User user);


    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "password", expression = "java(mapToEncodePasswd(editRequestDto, bCryptConverter))")
    UserDto mapToDtoForEdit(UserEditRequestDto editRequestDto, UUID userId, @Context BCryptConverter bCryptConverter);

    UserGetRequestDto mapToDtoWithId(String loginId);

    User mapToEntity(UserDto userDto);

    default Tel mapToTel(String value) {
        return Tel.valueOf(value);
    }

    default String mapToTel(Tel tel) {
        return tel.getTel();
    }

    default Email mapToEmail(String value) {
        return Email.valueOf(value);
    }

    default String mapToEmail(Email email) {
        return email.getEmail();
    }

    default UUID generateUUID(@Context UUIDGenerator uuidGenerator) {
        return uuidGenerator.generate();
    }

    default String mapToEncodePasswd(UserSaveRequestDto saveRequestDto, @Context BCryptConverter bCryptConverter) {
        return bCryptConverter.encode(saveRequestDto.getPassword());
    }

    default String mapToEncodePasswd(UserEditRequestDto editRequestDto, @Context BCryptConverter bCryptConverter) {
        return bCryptConverter.encode(editRequestDto.getPassword());
    }
}
