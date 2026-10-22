package org.progetti.accessdeskapi.mapper.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.progetti.accessdeskapi.dtos.request.CreateRequestSoftware;
import org.progetti.accessdeskapi.dtos.request.RequestResponse;
import org.progetti.accessdeskapi.models.request.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestResponse toDTO(Request entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "decidedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "requestedBy",  ignore = true)
    @Mapping(target = "status", ignore = true)
    Request toEntity(CreateRequestSoftware dto);

}
