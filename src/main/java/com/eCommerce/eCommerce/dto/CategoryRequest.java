package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name can have at most 100 characters")
    private String name;

    @Size(max = 500, message = "Description can have at most 500 characters")
    private String description;
}
