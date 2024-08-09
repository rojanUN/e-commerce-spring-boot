package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface UserService {

    ApiResponse allUsers();

    ApiResponse createAdministrator(RegisterUserDto input);

    public ApiResponse getAuthenticatedUser(Long userId);

}
