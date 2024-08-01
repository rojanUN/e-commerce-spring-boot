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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Override
    public ApiResponse getWishListByUserId(Long userId) {
        WishList wishList = wishListRepository.findByUserId(userId);
        if (wishList == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            wishList = new WishList();
            wishList.setUser(user);
            wishListRepository.save(wishList);
        }
        WishListResponse wishListResponse = mapToWishListResponse(wishList);
        return ResponseBuilder.buildSuccessResponse(wishListResponse);
    }


    @Override
    public ApiResponse addProductToWishList(Long userId, Long productId) {
        WishList wishList = getWishListByUserIdHelper(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new EcommerceException("PRO001"));

        WishListItem wishListItem = new WishListItem();
        wishListItem.setWishList(wishList);
        wishListItem.setProduct((product));

        wishList.getWishListItem().add(wishListItem);

        wishListRepository.save(wishList);

        WishListResponse wishListResponse = new WishListResponse();
        modelMapper.map(wishList, wishListResponse);

        return ResponseBuilder.buildSuccessResponse(wishListResponse);
    }

    @Override
    public ApiResponse removeProductFromWishList(Long userId, Long productId) {
        WishList wishList = getWishListByUserIdHelper(userId);
        wishList.getWishListItem().removeIf(item -> item.getProduct().getId().equals(productId));
        wishListRepository.save(wishList);
        return ResponseBuilder.buildSuccessResponse("message.wish.list.deleted.success");
    }


    //Helper methods
    private WishListResponse.WishListItemResponse mapToWishListItemResponse(WishListItem wishListItem) {
        WishListResponse.WishListItemResponse wishListItemResponse = new WishListResponse.WishListItemResponse();
        Product product = wishListItem.getProduct();
        wishListItemResponse.setProductId(product.getId());
        wishListItemResponse.setProductName(product.getName());
        wishListItemResponse.setProductDescription(product.getDescription());
        wishListItemResponse.setProductPrice(product.getPrice());
        wishListItemResponse.setProductCategory(product.getCategory().getName());
        return wishListItemResponse;
    }

    public WishList getWishListByUserIdHelper(Long userId) {
        WishList wishList = wishListRepository.findByUserId(userId);
        if (wishList == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            wishList = new WishList();
            wishList.setUser(user);
            wishList = wishListRepository.save(wishList);
        }
        return wishList;
    }

    private WishListResponse mapToWishListResponse(WishList wishList) {
        WishListResponse wishListResponse = new WishListResponse();
        wishListResponse.setId(wishList.getId());
        wishListResponse.setUserId(wishList.getUser().getId());
        wishListResponse.setWishListItems(
                wishList.getWishListItem().stream().map(this::mapToWishListItemResponse).toList()
        );
        return wishListResponse;
    }
}
