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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse submitReview(Long userId, ReviewRequest reviewRequest) {

        Product product = productRepository
                .findById(reviewRequest.getProductId())
                .orElseThrow(() -> new EcommerceException("PRO001"));
        List<Order> completedOrders = orderRepository.findByUserIdAndProductIdAndStatus(userId, reviewRequest.getProductId(), OrderStatus.COMPLETED);
        if ((completedOrders.isEmpty())) {
            throw new EcommerceException("ODR005");
        }

        boolean reviewExists = reviewRepository.existsByUserIdAndProductId(userId, reviewRequest.getProductId());
        if (reviewExists) {
            throw new EcommerceException("REV003");
        }

        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001")));
        review.setProduct(product);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        reviewRepository.save(review);

        return ResponseBuilder.buildSuccessResponse("message.review.submit.success");

    }

    @Override
    public ApiResponse removeReview(Long userId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EcommerceException("REV001"));

        if (!review.getUser().getId().equals(userId)) {
            throw new EcommerceException("REV002");
        }
        reviewRepository.delete(review);
        return ResponseBuilder.buildSuccessResponse("message.review.delete.success");

    }

    @Override
    public ApiResponse updateReview(Long userId, Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EcommerceException("REV001"));

        if (!review.getUser().getId().equals(userId)) {
            throw new EcommerceException("REV002");
        }

        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        reviewRepository.save(review);

        ReviewResponse reviewResponse = new ReviewResponse();
        modelMapper.map(review, reviewResponse);


        return ResponseBuilder.buildSuccessResponse(reviewResponse, "message.review.update.success");
    }

    @Override
    public ApiResponse findReviewByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .toList();

        return ResponseBuilder.buildSuccessResponse(reviewResponses, "message.review.fetch.success");
    }

}
