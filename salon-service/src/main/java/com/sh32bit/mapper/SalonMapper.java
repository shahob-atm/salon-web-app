package com.sh32bit.mapper;

import com.sh32bit.model.Salon;
import com.sh32bit.response.SalonResponse;

public class SalonMapper {
    public static SalonResponse mapToSalonResponse(Salon salon) {
        return new SalonResponse(
                salon.getId(),
                salon.getName(),
                salon.getAddress(),
                salon.getPhoneNumber(),
                salon.getEmail(),
                salon.getCity(),
                salon.isOpen(),
                salon.isHomeService(),
                salon.isActive(),
                salon.getOpenTime(),
                salon.getCloseTime(),
                salon.getImages(),
                salon.getOwnerId()
        );
    }
}
