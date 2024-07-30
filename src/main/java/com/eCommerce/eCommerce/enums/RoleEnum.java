package com.eCommerce.eCommerce.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("Merchant"),
    USER("Customer"),
    SUPER_ADMIN("Admin");

    private final String desc;

    RoleEnum(String desc) {
        this.desc = desc;
    }

}
