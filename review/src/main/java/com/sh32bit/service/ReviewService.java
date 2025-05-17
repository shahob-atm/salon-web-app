package com.sh32bit.service;

import com.sh32bit.model.Review;
import com.sh32bit.request.ReviewRequest;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.UserResponse;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsBySalonId(Long salonId);

    Review createReview(ReviewRequest req, UserResponse user, SalonResponse salon);

    Review updateReview(Long reviewId, String reviewText, double reviewRating, Long id) throws Exception;

    void deleteReview(Long reviewId, Long id) throws Exception;
}
