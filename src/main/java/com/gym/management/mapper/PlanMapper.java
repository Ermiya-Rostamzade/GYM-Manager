package com.gym.management.mapper;

import com.gym.management.dto.request.PlanCreateRequest;
import com.gym.management.dto.response.PlanResponse;
import com.gym.management.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Plan toEntity(PlanCreateRequest planCreateRequest);

    PlanResponse toResponse(Plan entity);
}
