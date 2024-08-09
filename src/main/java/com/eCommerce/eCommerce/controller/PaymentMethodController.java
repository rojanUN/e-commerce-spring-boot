package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.PaymentMethodRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/add")
    public ResponseEntity<Response> addPaymentMethod(@Valid @RequestBody PaymentMethodRequest paymentMethodRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentMethodService.addPaymentMethod(user.getId(), paymentMethodRequest));
    }

    @GetMapping("/")
    public ApiResponse getPaymentMethod(@AuthenticationPrincipal User user) {
        return paymentMethodService.findAllPaymentMethodByUserId(user.getId());
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse removePaymentMethod(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return paymentMethodService.removePaymentMethod(user.getId(), id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse updatePaymentMethod(@PathVariable Long id, @Valid @RequestBody PaymentMethodRequest paymentMethodRequest, @AuthenticationPrincipal User user) {
        return paymentMethodService.updatePaymentMethod(user.getId(), id, paymentMethodRequest);
    }

}
