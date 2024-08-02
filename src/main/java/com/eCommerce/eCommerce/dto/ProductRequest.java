package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name can have at most 100 characters")
    private String name;

    @Size(max = 500, message = "Description can have at most 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

}
