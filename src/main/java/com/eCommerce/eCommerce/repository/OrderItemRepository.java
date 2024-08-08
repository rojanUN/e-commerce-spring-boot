package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    OrderItem findOrderItemByOrderId(Long orderId);

    @Modifying
    @Query("UPDATE OrderItem o SET o.softDeleted = true WHERE o.product.id = :productId")
    void softDeleteByProductId(@Param("productId") Long productId);

}

