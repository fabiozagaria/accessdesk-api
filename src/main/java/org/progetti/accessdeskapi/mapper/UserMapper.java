package org.progetti.accessdeskapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.progetti.accessdeskapi.dtos.CreateRequestUser;
import org.progetti.accessdeskapi.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(CreateRequestUser dto);


    CreateRequestUser toDTO(User entity);
}
