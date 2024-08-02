package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.OrderRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse createOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequest, user.getId());
    }

}