package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.LoginUserDto;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface AuthenticationService {

    ApiResponse signup(RegisterUserDto input);

    User authenticate(LoginUserDto input);

}
