package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Response> getWishList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishListService.getWishListByUserId(user.getId()));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Response> addProductToWishList(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishListService.addProductToWishList(user.getId(), productId));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Response> removeProductFromWishList(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(wishListService.removeProductFromWishList(user.getId(), productId));
    }

}
