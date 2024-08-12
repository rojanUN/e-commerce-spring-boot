package com.eCommerce.eCommerce.dto.response;

import com.eCommerce.eCommerce.enums.RoleEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private RoleEnum roleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
