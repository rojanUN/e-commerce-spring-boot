package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WishListRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

}
