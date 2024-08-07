package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
@PreAuthorize("hasRole('USER')")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public ApiResponse submitReview(@Valid @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
        return reviewService.submitReview(user.getId(), reviewRequest);
    }

    @DeleteMapping("/{id}/remove")
    public ApiResponse removeReview(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return reviewService.removeReview(user.getId(), id);
    }

    @PutMapping("/{id}/update")
    public ApiResponse updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
        return reviewService.updateReview(user.getId(), id, reviewRequest);
    }

    @GetMapping("/find")
    public ApiResponse findReview(@AuthenticationPrincipal User user) {
        return reviewService.findReviewByUser(user.getId());
    }

}
