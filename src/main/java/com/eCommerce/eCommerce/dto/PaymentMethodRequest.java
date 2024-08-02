package com.eCommerce.eCommerce.dto;

import com.eCommerce.eCommerce.enums.PaymentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class PaymentMethodRequest {

    @NotNull(message = "Payment type cannot be null")
    private PaymentType paymentType;

    @NotBlank(message = "Provider cannot be blank")
    @Size(max = 100, message = "Provider can have at most 100 characters")
    private String provider;

    @NotBlank(message = "Account number cannot be blank")
    @Size(max = 20, message = "Account number can have at most 20 characters")
    private String accountNumber;

    @NotNull(message = "Expiry date cannot be null")
    @Future(message = "Expiry date must be in the future")
    private Date expiryDate;

    @NotNull(message = "Default status cannot be null")
    private Boolean isDefault;

}
