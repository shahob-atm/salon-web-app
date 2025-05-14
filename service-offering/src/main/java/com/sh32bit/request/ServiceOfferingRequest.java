package com.sh32bit.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOfferingRequest {
    private String name;
    private String description;
    private int price;
    private int duration;
    private boolean available;
    private String image;
}
