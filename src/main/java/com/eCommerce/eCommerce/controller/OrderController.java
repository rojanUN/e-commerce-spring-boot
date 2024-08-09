package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.OrderRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Response> createOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest, user.getId()));
    }

    @GetMapping("find-my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response> findOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.findOrders(user.getId()));
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.cancelOrder(user.getId(), orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/complete")
    public ResponseEntity<Response> completeOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }


}