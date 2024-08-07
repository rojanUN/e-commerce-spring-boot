package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
