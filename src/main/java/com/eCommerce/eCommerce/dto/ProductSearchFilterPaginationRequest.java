package com.eCommerce.eCommerce.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchFilterPaginationRequest extends PaginationRequest {

    private String searchText;

    private String createdDateFrom;

    private String createdDateTo;

    private String stockFloor;

    private String stockCeiling;

}
