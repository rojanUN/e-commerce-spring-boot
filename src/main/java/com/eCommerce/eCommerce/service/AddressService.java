package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.AddressRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface AddressService {

    ApiResponse addAddress(Long userId, AddressRequest addressRequest);

    ApiResponse removeAddress(Long userId, Long addressId);

    ApiResponse updateAddress(Long userId, Long addressId, AddressRequest addressRequest);

    ApiResponse findAllAddressByUserId(Long userId);
}
