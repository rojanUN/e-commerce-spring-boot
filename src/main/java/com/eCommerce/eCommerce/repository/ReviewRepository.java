package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByIdAndUserId(Long userId, Long reviewId);

    boolean existsByUserIdAndProductId(Long userId, long productId);

    List<Review> findByUserId(Long userId);
    
}
