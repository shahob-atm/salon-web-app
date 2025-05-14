package com.sh32bit.mapper;

import com.sh32bit.model.ServiceOffering;
import com.sh32bit.response.ServiceOfferingResponse;

public class ServiceOfferingMapper {
    public static ServiceOfferingResponse toServiceOfferingResponse(ServiceOffering serviceOffering) {
        return ServiceOfferingResponse.builder()
                .id(serviceOffering.getId())
                .name(serviceOffering.getName())
                .description(serviceOffering.getDescription())
                .price(serviceOffering.getPrice())
                .duration(serviceOffering.getDuration())
                .salonId(serviceOffering.getSalonId())
                .available(serviceOffering.isAvailable())
                .categoryId(serviceOffering.getCategoryId())
                .image(serviceOffering.getImage())
                .build();
    }
}
