package com.eCommerce.eCommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginUserDto {
    private String email;

    private String password;
}
