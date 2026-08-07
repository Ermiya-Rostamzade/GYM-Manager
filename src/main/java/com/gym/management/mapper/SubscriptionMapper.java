package com.gym.management.mapper;

import com.gym.management.dto.response.SubscriptionResponse;
import com.gym.management.entity.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlanMapper.class})
public interface SubscriptionMapper {

    @Mapping(target = "status", expression = "java(entity.getStatus != null ? entity.getStatus.name() : null)")
    SubscriptionResponse toResponse(UserSubscription entity);
}
