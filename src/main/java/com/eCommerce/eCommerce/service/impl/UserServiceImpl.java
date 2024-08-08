package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.entity.Role;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.enums.RoleEnum;
import com.eCommerce.eCommerce.repository.RoleRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public List<User> allUsers() {
        log.info("Fetching all users");
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        log.info("Fetched {} users", users.size());
        return users;
    }

    public User createAdministrator(RegisterUserDto input) {
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

        log.info("Created administrator with ID: {}", savedUser.getId());
        return savedUser;
    }
}
