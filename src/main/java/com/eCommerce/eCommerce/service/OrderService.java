package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.OrderRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface OrderService {

    ApiResponse createOrder(OrderRequest orderRequest, Long userId);

    ApiResponse findOrders(Long userId);

    ApiResponse cancelOrder(Long userId, Long orderId);
}
