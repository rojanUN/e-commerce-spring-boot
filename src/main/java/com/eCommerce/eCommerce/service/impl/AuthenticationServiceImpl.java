package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.LoginUserDto;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.dto.response.UserResponse;
import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.enums.RoleEnum;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.RoleRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;


    @Override
    public ApiResponse signup(RegisterUserDto input) {
        log.info("Signing up user with email: {}", input.getEmail());
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(input.getEmail()).matches()) {
            log.error("Invalid email format: {}", input.getEmail());
            return ResponseBuilder.buildResponse("Invalid Email format", HttpStatus.BAD_REQUEST);

        }
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            log.error("Role USER not found in the database");
            throw new EcommerceException("ROL001");
        }

        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(optionalRole.get());

        User savedUser = userRepository.save(user);

        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);
        log.info("User signed up successfully with email: {}", input.getEmail());
        return ResponseBuilder.buildSuccessResponse(userResponse);
    }

    @Override
    public User authenticate(LoginUserDto input) {
        log.info("Authenticating user with email: {}", input.getEmail());

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", input.getEmail());
                    return new EcommerceException("USR001");
                });
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for email: {}", input.getEmail(), e);
            throw e;
        }

        log.info("User authenticated successfully with email: {}", input.getEmail());
        return user;
    }
}
