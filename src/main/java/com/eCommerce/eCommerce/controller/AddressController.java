package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.AddressRequest;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.AddressService;
import lombok.AllArgsConstructor;
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
    public ApiResponse addAddress(@RequestBody AddressRequest addressRequest, @AuthenticationPrincipal User user) {
        return addressService.addAddress(user.getId(), addressRequest);
    }

    @GetMapping("/")
    public ApiResponse getAddress(@AuthenticationPrincipal User user) {
        return addressService.findAllAddressByUserId(user.getId());
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse removeAddress(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return addressService.removeAddress(user.getId(), id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateAddress(@PathVariable Long id, @RequestBody AddressRequest addressRequest, @AuthenticationPrincipal User user) {
        return addressService.updateAddress(user.getId(), id, addressRequest);
    }

}
