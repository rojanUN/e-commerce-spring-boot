package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByIdAndUserId(Long userId, Long reviewId);

    boolean existsByUserIdAndProductId(Long userId, long productId);

    List<Review> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE Review r SET r.softDeleted = true WHERE r.product.id = :productId")
    void softDeleteByProductId(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double calculateAverageRatingByProductId(Long productId);

}
