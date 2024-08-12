package com.eCommerce.eCommerce.dto;

import lombok.Data;

@Data
public class PaginationRequest {
    private int pageNo;
    private int pageSize;
    private String sortBy;
    private String direction;
}
