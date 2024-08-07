package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.ReviewRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    @Override
    public ApiResponse submitReview(Long userId, ReviewRequest request) {

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new EcommerceException("PRO001"));
        List<Order> completedOrders = orderRepository.findByUserIdAndProductIdAndStatus(userId, request.getProductId(), OrderStatus.COMPLETED);
        if ((completedOrders.isEmpty())) {
            throw
                    new EcommerceException("ODR005");
        }

        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001")));
        review.setProduct(product);
        review.setComment(request.getComment());
        review.setRating(request.getRating());

        reviewRepository.save(review);

        return ResponseBuilder.buildSuccessResponse("message.review.submit.success");

    }

}
