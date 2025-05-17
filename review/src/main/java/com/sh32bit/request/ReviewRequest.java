package com.sh32bit.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private String reviewText;
    private double reviewRating;
    private List<String> productImages;
}
