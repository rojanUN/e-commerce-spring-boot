package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<Response> submitReview(@Valid @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.submitReview(user.getId(), reviewRequest));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Response> removeReview(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.removeReview(user.getId(), id));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/update")
    public ResponseEntity<Response> updateReview(@PathVariable Long id, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.updateReview(user.getId(), id, reviewRequest));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/find")
    public ResponseEntity<Response> findReview(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.findReviewByUser(user.getId()));
    }

}
