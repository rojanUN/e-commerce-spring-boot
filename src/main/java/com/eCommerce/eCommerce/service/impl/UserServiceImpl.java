package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.ChangePasswordRequest;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.dto.response.UserResponse;
import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.enums.RoleEnum;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.RoleRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Override
    public ApiResponse allUsers() {
        log.info("Fetching all users");
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        log.info("Fetched {} users", users.size());
        List<UserResponse> userResponses = users.stream().map(
                        user -> modelMapper.map(user, UserResponse.class))
                .toList();
        return ResponseBuilder.buildSuccessResponse(userResponses);
    }

    @Override
    public ApiResponse getAuthenticatedUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001"));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return ResponseBuilder.buildSuccessResponse(userResponse);
    }

    @Override
    public ApiResponse changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new EcommerceException("USR002");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new EcommerceException("USR003");
        }

        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setLastPasswordChange(LocalDateTime.now());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return ResponseBuilder.buildSuccessResponse("message.user.change.password.success");
    }

    @Override
    public ApiResponse createAdministrator(RegisterUserDto input) {
        log.info("Creating administrator with email: {}", input.getEmail());

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            log.warn("Admin role not found");
            return null;
        }

        var user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(optionalRole.get());

        User savedUser = userRepository.save(user);
        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
        log.info("Created administrator with ID: {}", savedUser.getId());
        return ResponseBuilder.buildSuccessResponse(userResponse);
    }


}
