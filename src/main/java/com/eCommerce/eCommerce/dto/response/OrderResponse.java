package com.eCommerce.eCommerce.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class OrderResponse {

    private Long id;

    private Long userId;

    private Long shippingAddressId;

    private Long paymentMethodId;

    private List<OrderItemResponse> orderItems;

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalAmount;

}
