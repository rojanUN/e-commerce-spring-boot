package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReviewRequest {

    @NotNull
    private long productId;

    private String comment;

    @Min(value = 1, message = "rating can't be less than 1")
    @Max(value = 5, message = "rating can't be more than 5")
    @NotNull
    private Integer rating;

}
