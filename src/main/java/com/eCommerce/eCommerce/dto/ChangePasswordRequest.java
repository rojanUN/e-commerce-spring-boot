package com.eCommerce.eCommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Old Password cannot be Blank")
    private String oldPassword;
    @NotBlank(message = "New Password cannot be Blank")
    private String newPassword;
    @NotBlank(message = "Confirm Password cannot be Blank")
    private String confirmPassword;

}
