package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.OrderRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse createOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequest, user.getId());
    }

    @GetMapping("find-my-orders")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse findOrders(@AuthenticationPrincipal User user) {
        return orderService.findOrders(user.getId());
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        return orderService.cancelOrder(user.getId(), orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/complete")
    public ApiResponse completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }


}