package com.sh32bit.mapper;

import com.sh32bit.model.Review;
import com.sh32bit.response.ReviewResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.UserResponse;

public class ReviewMapper {
    public static ReviewResponse toReviewResponse(Review review, UserResponse user, SalonResponse salon) {
        return ReviewResponse.builder()
                .id(review.getId())
                .user(user)
                .salon(salon)
                .reviewText(review.getReviewText())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
