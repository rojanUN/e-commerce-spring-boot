package com.eCommerce.eCommerce.dto.response;

import com.eCommerce.eCommerce.entity.WishListItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Data
public class WishListResponse {

    private Long id;

    private Long userId;

    private List<WishListItemResponse> wishListItems;


    @Getter
    @Setter
    public static class WishListItemResponse {
        private Long productId;
        private String productName;
        private String productDescription;
        private BigDecimal productPrice;
        private String productCategory;
    }
}
