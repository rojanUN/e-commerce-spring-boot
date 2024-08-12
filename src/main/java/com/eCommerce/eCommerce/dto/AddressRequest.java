package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street can have at most 255 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City can have at most 100 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State can have at most 100 characters")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country can have at most 100 characters")
    private String country;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 20, message = "Postal code can have at most 20 characters")
    private String postalCode;

    @NotNull(message = "Default status cannot be null")
    private Boolean isDefault;

}

