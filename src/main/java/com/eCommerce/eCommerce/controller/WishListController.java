package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/wish-lists")
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/")
    public ApiResponse getWishList(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return wishListService.getWishListByUserId(user.getId());
    }

    @PostMapping("/add/{productId}")
    public ApiResponse addProductToWishList(@PathVariable Long productId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return wishListService.addProductToWishList(user.getId(), productId);
    }


    @DeleteMapping("/remove/{productId}")
    public ApiResponse removeProductFromWishList(@PathVariable Long productId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return wishListService.removeProductFromWishList(user.getId(), productId);
    }

}
