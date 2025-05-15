package com.sh32bit.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOfferingResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int duration;
    private Long salonId;
    private boolean available;
    private Long categoryId;
    private String image;
}
