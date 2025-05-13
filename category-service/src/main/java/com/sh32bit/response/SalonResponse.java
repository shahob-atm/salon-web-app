package com.sh32bit.response;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalonResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String city;
    private boolean isOpen;
    private boolean homeService;
    private boolean active;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<String> images;
    private Long ownerI;
}
