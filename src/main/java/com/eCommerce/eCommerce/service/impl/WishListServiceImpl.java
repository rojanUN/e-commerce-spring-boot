package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.response.WishListResponse;
import com.eCommerce.eCommerce.entity.Product;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.entity.WishList;
import com.eCommerce.eCommerce.entity.WishListItem;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.ProductRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.repository.WishListRepository;
import com.eCommerce.eCommerce.service.WishListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse getWishListByUserId(Long userId) {
        log.info("Fetching wishlist for user ID: {}", userId);
        WishList wishList = wishListRepository.findByUserId(userId);
        if (wishList == null) {
            log.info("No wishlist found for user ID: {}. Creating a new wishlist.", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User with ID {} not found", userId);
                        return new EcommerceException("USR001");
                    });
            wishList = new WishList();
            wishList.setUser(user);
            wishListRepository.save(wishList);
            log.info("Wishlist created successfully for user ID: {}", userId);
        }
        WishListResponse wishListResponse = mapToWishListResponse(wishList);
        log.info("Wishlist fetched successfully for user ID: {}", userId);
        return ResponseBuilder.buildSuccessResponse(wishListResponse);
    }

    @Override
    public ApiResponse addProductToWishList(Long userId, Long productId) {
        log.info("Adding product ID: {} to wishlist for user ID: {}", productId, userId);
        WishList wishList = getWishListByUserIdHelper(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", productId);
                    return new EcommerceException("PRO001");
                });

        WishListItem wishListItem = new WishListItem();
        wishListItem.setWishList(wishList);
        wishListItem.setProduct(product);
        wishList.getWishListItem().add(wishListItem);

        wishListRepository.save(wishList);
        log.info("Product ID: {} added to wishlist for user ID: {}", productId, userId);

        WishListResponse wishListResponse = new WishListResponse();
        modelMapper.map(wishList, wishListResponse);
        return ResponseBuilder.buildSuccessResponse(wishListResponse);
    }

    @Override
    public ApiResponse removeProductFromWishList(Long userId, Long productId) {
        log.info("Removing product ID: {} from wishlist for user ID: {}", productId, userId);
        WishList wishList = getWishListByUserIdHelper(userId);
        boolean removed = wishList.getWishListItem().removeIf(item -> item.getProduct().getId().equals(productId));
        if (removed) {
            log.info("Product ID: {} removed from wishlist for user ID: {}", productId, userId);
        } else {
            log.warn("Product ID: {} not found in wishlist for user ID: {}", productId, userId);
        }
        wishListRepository.save(wishList);
        return ResponseBuilder.buildSuccessResponse("message.wish.list.deleted.success");
    }

    // Helper methods
    private WishListResponse.WishListItemResponse mapToWishListItemResponse(WishListItem wishListItem) {
        Product product = wishListItem.getProduct();
        log.debug("Mapping product ID: {} to WishListItemResponse", product.getId());
        WishListResponse.WishListItemResponse wishListItemResponse = new WishListResponse.WishListItemResponse();
        wishListItemResponse.setProductId(product.getId());
        wishListItemResponse.setProductName(product.getName());
        wishListItemResponse.setProductDescription(product.getDescription());
        wishListItemResponse.setProductPrice(product.getPrice());
        wishListItemResponse.setProductCategory(product.getCategory().getName());
        return wishListItemResponse;
    }

    public WishList getWishListByUserIdHelper(Long userId) {
        log.debug("Fetching wishlist for user ID: {}", userId);
        WishList wishList = wishListRepository.findByUserId(userId);
        if (wishList == null) {
            log.info("No wishlist found for user ID: {}. Creating a new wishlist.", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User with ID {} not found", userId);
                        return new RuntimeException("User not found");
                    });
            wishList = new WishList();
            wishList.setUser(user);
            wishList = wishListRepository.save(wishList);
            log.info("Wishlist created successfully for user ID: {}", userId);
        }
        return wishList;
    }

    private WishListResponse mapToWishListResponse(WishList wishList) {
        log.debug("Mapping wishlist ID: {} to WishListResponse", wishList.getId());
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setId(wishList.getId());
        wishListResponse.setUserId(wishList.getUser().getId());
        wishListResponse.setWishListItems(
                wishList.getWishListItem().stream().map(this::mapToWishListItemResponse).toList()
        );
        return wishListResponse;
    }
}
