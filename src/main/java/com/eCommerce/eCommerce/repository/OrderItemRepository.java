package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    OrderItem findOrderItemByOrderId(Long orderId);

}

