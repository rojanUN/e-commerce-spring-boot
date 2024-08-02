package com.eCommerce.eCommerce.dto;

import com.eCommerce.eCommerce.enums.PaymentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class PaymentMethodRequest {

    private PaymentType paymentType;

    private String provider;

    private String accountNumber;

    private Date expiryDate;

    private Boolean isDefault;

}
