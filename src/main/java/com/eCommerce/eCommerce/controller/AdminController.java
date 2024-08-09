package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response> createAdministrator(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(userService.createAdministrator(registerUserDto));
    }
}

