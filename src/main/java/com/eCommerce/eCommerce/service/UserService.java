package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.ChangePasswordRequest;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface UserService {

    ApiResponse allUsers();

    ApiResponse createAdministrator(RegisterUserDto input);

    ApiResponse getAuthenticatedUser(Long userId);

    ApiResponse changePassword(Long userId, ChangePasswordRequest changePasswordRequest);

}
