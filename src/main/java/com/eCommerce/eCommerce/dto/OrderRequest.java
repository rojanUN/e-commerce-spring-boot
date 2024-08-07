package com.eCommerce.eCommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class OrderRequest {

    @NotNull(message = "Shipping address ID cannot be null")
    private Long shippingAddressId;

    @NotNull(message = "Payment method ID cannot be null")
    private Long paymentMethodId;

    @NotNull(message = "Order items cannot be null")
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequest> orderItems;

}

