package com.gym.management.mapper;

import com.gym.management.dto.response.TrafficLogResponse;
import com.gym.management.entity.TrafficLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrafficLogMapper {

    @Mapping(target = "message", ignore = true)
    @Mapping(target = "entryMethod", source = "method")
    TrafficLogResponse toResponse(TrafficLog entity);


}
