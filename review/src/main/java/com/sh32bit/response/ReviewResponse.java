package com.sh32bit.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long property;
    private UserResponse user;
    private SalonResponse salon;
    private String reviewText;
    private double rating;
    private LocalDateTime createdAt;
}
