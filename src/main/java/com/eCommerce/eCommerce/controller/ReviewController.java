package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public ApiResponse submitReview(@Valid @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal User user){
        return  reviewService.submitReview(user.getId(), reviewRequest);
    }

}
