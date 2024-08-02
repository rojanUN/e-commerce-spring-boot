package com.eCommerce.eCommerce.dto.response;

import com.eCommerce.eCommerce.enums.PaymentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class PaymentMethodResponse {

    private PaymentType paymentType;

    private String provider;

    private String accountNumber;

    private Date expiryDate;

    private Boolean isDefault;


}
