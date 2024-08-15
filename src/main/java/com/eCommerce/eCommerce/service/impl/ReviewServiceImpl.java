package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.ReviewRequest;
import com.eCommerce.eCommerce.dto.response.ReviewResponse;
import com.eCommerce.eCommerce.entity.Order;
import com.eCommerce.eCommerce.entity.Product;
import com.eCommerce.eCommerce.entity.Review;
import com.eCommerce.eCommerce.enums.OrderStatus;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.OrderRepository;
import com.eCommerce.eCommerce.repository.ProductRepository;
import com.eCommerce.eCommerce.repository.ReviewRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse submitReview(Long userId, ReviewRequest reviewRequest) {
        log.info("Submitting review for product ID: {} by user ID: {}", reviewRequest.getProductId(), userId);

        Product product = productRepository
                .findById(reviewRequest.getProductId())
                .orElseThrow(() -> {
                    log.error("Product with ID: {} not found", reviewRequest.getProductId());
                    return new EcommerceException("PRO001", HttpStatus.NOT_FOUND);
                });

        List<Order> completedOrders = orderRepository.findByUserIdAndProductIdAndStatus(userId, reviewRequest.getProductId(), OrderStatus.COMPLETED);
        if (completedOrders.isEmpty()) {
            log.error("No completed orders found for user ID: {} and product ID: {}", userId, reviewRequest.getProductId());
            throw new EcommerceException("ODR005", HttpStatus.FORBIDDEN);
        }

        boolean reviewExists = reviewRepository.existsByUserIdAndProductId(userId, reviewRequest.getProductId());
        if (reviewExists) {
            log.error("Review already exists for user ID: {} and product ID: {}", userId, reviewRequest.getProductId());
            throw new EcommerceException("REV003", HttpStatus.FORBIDDEN);
        }

        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID: {} not found", userId);
            return new EcommerceException("USR001", HttpStatus.NOT_FOUND);
        }));
        review.setProduct(product);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        reviewRepository.save(review);
        log.info("Review submitted successfully for product ID: {} by user ID: {}", reviewRequest.getProductId(), userId);

        return ResponseBuilder.buildSuccessResponse("message.review.submit.success");
    }

    @Override
    public ApiResponse removeReview(Long userId, Long reviewId) {
        log.info("Removing review with ID: {} by user ID: {}", reviewId, userId);

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error("Review with ID: {} not found", reviewId);
            return new EcommerceException("REV001", HttpStatus.NOT_FOUND);
        });

        if (!review.getUser().getId().equals(userId)) {
            log.error("User ID: {} does not match review owner ID: {}", userId, review.getUser().getId());
            throw new EcommerceException("REV002", HttpStatus.FORBIDDEN);
        }

        reviewRepository.delete(review);
        log.info("Review with ID: {} removed successfully by user ID: {}", reviewId, userId);

        return ResponseBuilder.buildSuccessResponse("message.review.delete.success");
    }

    @Override
    public ApiResponse updateReview(Long userId, Long reviewId, ReviewRequest reviewRequest) {
        log.info("Updating review with ID: {} by user ID: {}", reviewId, userId);

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error("Review with ID: {} not found", reviewId);
            return new EcommerceException("REV001", HttpStatus.NOT_FOUND);
        });

        if (!review.getUser().getId().equals(userId)) {
            log.error("User ID: {} does not match review owner ID: {}", userId, review.getUser().getId());
            throw new EcommerceException("REV002", HttpStatus.FORBIDDEN);
        }

        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        reviewRepository.save(review);

        ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);
        log.info("Review with ID: {} updated successfully by user ID: {}", reviewId, userId);

        return ResponseBuilder.buildSuccessResponse(reviewResponse, "message.review.update.success");
    }

    @Override
    public ApiResponse findReviewByUser(Long userId) {
        log.info("Fetching reviews for user ID: {}", userId);

        List<Review> reviews = reviewRepository.findByUserId(userId);
        if (reviews.isEmpty()) {
            log.info("No reviews found for user ID: {}", userId);
        }

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .toList();

        return ResponseBuilder.buildSuccessResponse(reviewResponses, "message.review.fetch.success");
    }
}
