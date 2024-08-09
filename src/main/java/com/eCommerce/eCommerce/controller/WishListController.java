package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/wish-lists")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/")
    public ApiResponse getWishList(@AuthenticationPrincipal User user) {
        return wishListService.getWishListByUserId(user.getId());
    }

    @PostMapping("/add/{productId}")
    public ApiResponse addProductToWishList(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        return wishListService.addProductToWishList(user.getId(), productId);
    }

    @DeleteMapping("/remove/{productId}")
    public ApiResponse removeProductFromWishList(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        return wishListService.removeProductFromWishList(user.getId(), productId);
    }

}
