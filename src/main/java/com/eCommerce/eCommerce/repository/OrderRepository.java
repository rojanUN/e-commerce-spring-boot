package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Order;
import com.eCommerce.eCommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrdersByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status AND EXISTS (SELECT 1 FROM o.orderItems oi WHERE oi.product.id = :productId)")
    List<Order> findByUserIdAndProductIdAndStatus(@Param("userId") Long userId, @Param("productId") Long productId, @Param("status") OrderStatus status);

}

