package com.sh32bit.controller;

import com.sh32bit.mapper.ReviewMapper;
import com.sh32bit.model.Review;
import com.sh32bit.request.ReviewRequest;
import com.sh32bit.response.ReviewResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.ReviewService;
import com.sh32bit.service.client.SalonFeignClient;
import com.sh32bit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserFeignClient userService;
    private final SalonFeignClient salonService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(
            @PathVariable Long salonId) {

        List<Review> reviews = reviewService.getReviewsBySalonId(salonId);

        List<ReviewResponse> res = reviews.stream().map((review) ->
                {
                    UserResponse user = null;
                    SalonResponse salon = null;
                    try {
                        user = userService.getUserById(review.getUserId()).getBody();
                        salon = salonService.getSalonById(review.getSalonId()).getBody();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return ReviewMapper.toReviewResponse(review, user, salon);
                }
        ).toList();

        return ResponseEntity.ok(res);
    }

    @PostMapping("/salon/{salonId}")
    public ResponseEntity<ReviewResponse> writeReview(
            @RequestBody ReviewRequest req,
            @PathVariable Long salonId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        UserResponse user = userService.getUserByJwtToken(jwt).getBody();
        SalonResponse salon = salonService.getSalonById(salonId).getBody();

        Review review = reviewService.createReview(
                req, user, salon
        );

        ReviewResponse res = ReviewMapper.toReviewResponse(review, user, salon);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody ReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt)
            throws Exception {

        UserResponse user = userService.getUserByJwtToken(jwt).getBody();

        assert user != null;

        Review review = reviewService.updateReview(
                reviewId,
                req.getReviewText(),
                req.getReviewRating(),
                user.id()
        );

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserResponse user = userService.getUserByJwtToken(jwt).getBody();

        assert user != null;
        reviewService.deleteReview(reviewId, user.id());
        return ResponseEntity.ok("Review deleted successfully");
    }
}
