package com.eCommerce.eCommerce.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
class OrderItemResponse {

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

}
