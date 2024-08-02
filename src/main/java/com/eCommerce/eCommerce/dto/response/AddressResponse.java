package com.eCommerce.eCommerce.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AddressResponse {

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Boolean isDefault;

}
