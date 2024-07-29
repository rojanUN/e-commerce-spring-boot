package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterUserDto {
    @NotEmpty(message = "The full name is required.")
    @Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
    private String fullName;

    @NotEmpty
    @Size(min = 8, max = 40, message = "The password must be at least 8 characters.")
    private String password;

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.")
    private String email;
}
