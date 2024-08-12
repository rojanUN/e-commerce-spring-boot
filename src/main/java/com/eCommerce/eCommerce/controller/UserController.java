package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ChangePasswordRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> authenticatedUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getAuthenticatedUser(currentUser.getId()));
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<Response> changePassword(@AuthenticationPrincipal User user, ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(userService.changePassword(user.getId(), changePasswordRequest));
    }
}
