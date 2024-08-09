package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.AddressRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<Response> addAddress(@Valid @RequestBody AddressRequest addressRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressService.addAddress(user.getId(), addressRequest));
    }

    @GetMapping("/")
    public ResponseEntity<Response> getAddress(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressService.findAllAddressByUserId(user.getId()));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Response> removeAddress(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(addressService.removeAddress(user.getId(), id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest addressRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressService.updateAddress(user.getId(), id, addressRequest));
    }

}
