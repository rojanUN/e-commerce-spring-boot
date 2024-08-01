package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
