package com.eCommerce.eCommerce.model;

@FunctionalInterface
public interface Codes {

    ApiError pick(String code);
}
