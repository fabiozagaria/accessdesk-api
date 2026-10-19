package org.progetti.accessdeskapi.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.progetti.accessdeskapi.dtos.register.CreateRequestUser;
import org.progetti.accessdeskapi.dtos.register.RegisterResponse;
import org.progetti.accessdeskapi.models.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(CreateRequestUser dto);


    RegisterResponse toDTO(User entity);
}
