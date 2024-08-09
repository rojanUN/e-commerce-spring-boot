package com.eCommerce.eCommerce.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductSearchFilterPaginationRequest extends PaginationRequest {

    private String searchText;

    private String createdDateFrom;

    private String createdDateTo;

    private BigDecimal priceFloor;

    private BigDecimal priceCeiling;

    private List<Long> categoryIds;

}
