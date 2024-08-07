package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface ReviewService {

    ApiResponse submitReview(Long userId, ReviewRequest request);

}
