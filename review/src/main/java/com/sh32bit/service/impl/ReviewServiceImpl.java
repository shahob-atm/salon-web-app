package com.sh32bit.service.impl;

import com.sh32bit.model.Review;
import com.sh32bit.repository.ReviewRepository;
import com.sh32bit.request.ReviewRequest;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsBySalonId(Long salonId) {
        return reviewRepository.findReviewsBySalonId(salonId);
    }

    @Override
    public Review createReview(ReviewRequest req, UserResponse user, SalonResponse salon) {
        Review review = Review.builder()
                .reviewText(req.getReviewText())
                .rating(req.getReviewRating())
                .userId(user.id())
                .salonId(salon.id())
                .build();

        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double reviewRating, Long id) throws Exception {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review Not found"));

        if (!Objects.equals(review.getUserId(), id)) {
            throw new AuthenticationException("You do not have permission to delete this review");
        }

        review.setReviewText(reviewText);
        review.setRating(reviewRating);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long id) throws Exception {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review Not found"));
        if (!Objects.equals(review.getUserId(), id)) {
            throw new AuthenticationException("You do not have permission to delete this review");
        }
        reviewRepository.delete(review);
    }
}
