package com.gym.management.mapper;

import com.gym.management.dto.response.SubscriptionResponse;
import com.gym.management.entity.UserSubscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlanMapper.class})
public interface SubscriptionMapper {

    SubscriptionResponse toResponse(UserSubscription entity);
}
