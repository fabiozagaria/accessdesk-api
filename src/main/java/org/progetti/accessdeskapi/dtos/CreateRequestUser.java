package org.progetti.accessdeskapi.dtos;

import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public record CreateRequestUser(
        String username,
        String password
) {

}
