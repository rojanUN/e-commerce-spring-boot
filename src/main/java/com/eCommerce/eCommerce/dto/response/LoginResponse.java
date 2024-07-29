package com.eCommerce.eCommerce.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LoginResponse {
    @Getter
    private String token;

    private long expiresIn;

}
