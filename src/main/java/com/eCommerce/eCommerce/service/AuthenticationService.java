package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.LoginUserDto;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface AuthenticationService {

    ApiResponse signup(RegisterUserDto input);

    ApiResponse authenticate(LoginUserDto input);

}
