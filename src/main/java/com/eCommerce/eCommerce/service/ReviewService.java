package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface ReviewService {

    ApiResponse submitReview(Long userId, ReviewRequest reviewRequest);

    ApiResponse removeReview(Long userId, Long reviewId);

    ApiResponse updateReview(Long userId, Long reviewId, ReviewRequest reviewRequest);

    ApiResponse findReviewByUser(Long userId);

}
