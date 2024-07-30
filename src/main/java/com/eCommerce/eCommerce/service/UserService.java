package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.entity.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();
    User createAdministrator(RegisterUserDto input);
}
