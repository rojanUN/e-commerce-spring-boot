package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.dto.LoginUserDto;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.enums.RoleEnum;
import com.eCommerce.eCommerce.repository.RoleRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto input) {
        log.info("Signing up user with email: {}", input.getEmail());

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            log.error("Role USER not found in the database");
            return null;
        }

        var user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(optionalRole.get());

        User savedUser = userRepository.save(user);
        log.info("User signed up successfully with email: {}", input.getEmail());
        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        log.info("Authenticating user with email: {}", input.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (Exception e) {
            log.error("Authentication failed for email: {}", input.getEmail(), e);
            throw e;
        }

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", input.getEmail());
                    return new RuntimeException("User not found");
                });

        log.info("User authenticated successfully with email: {}", input.getEmail());
        return user;
    }
}
