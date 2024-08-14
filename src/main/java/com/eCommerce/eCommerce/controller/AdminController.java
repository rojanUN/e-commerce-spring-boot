package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.AuditLogService;
import com.eCommerce.eCommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {
    private final UserService userService;
    private final AuditLogService auditLogService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response> createAdministrator(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(userService.createAdministrator(registerUserDto));
    }

    @GetMapping("/find-audit-logs")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response> findAuditLogs() {
        return ResponseEntity.ok(auditLogService.findAuditLogs());
    }

}

