package com.sh32bit.request;

import java.time.LocalTime;
import java.util.List;

public record SalonRequest(
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
        List<String> images
) {
}
