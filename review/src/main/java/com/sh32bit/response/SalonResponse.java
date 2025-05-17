package com.sh32bit.response;

import java.time.LocalTime;
import java.util.List;

public record SalonResponse(
        Long id,
        String name,
        String address,
        String phoneNumber,
        String email,
        String city,
        boolean isOpen,
        boolean homeService,
        boolean active,
        LocalTime openTime,
        LocalTime closeTime,
        List<String> images,
        Long ownerId
) {
}
