package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.model.ApiResponse;

public interface WishListService {

    ApiResponse getWishListByUserId(Long userId);

    ApiResponse addProductToWishList(Long userId, Long productId);

    ApiResponse removeProductFromWishList(Long userId, Long productId);

}
