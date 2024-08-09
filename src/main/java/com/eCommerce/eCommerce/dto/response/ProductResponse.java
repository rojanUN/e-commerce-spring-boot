package com.eCommerce.eCommerce.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductResponse {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private boolean softDeleted;
}
