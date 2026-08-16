package com.gym.management.mapper;

import com.gym.management.dto.response.PaymentHistoryResponse;
import com.gym.management.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "subscriptionId", source = "subscription.id")
    PaymentHistoryResponse toHistoryResponse(Payment entity);

    //خود مپ استراکت میاد روی متد بالایی حلقه میزنه و داخل یک لیست تمام تاریخچه رو ذخیره می کنه میشد داخل لایه سرویس هم نوشت
    List<PaymentHistoryResponse> toHistoryResponseList(List<Payment> entities);

}
