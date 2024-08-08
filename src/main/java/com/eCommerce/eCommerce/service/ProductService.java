package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.ProductRequest;
import com.eCommerce.eCommerce.dto.ProductSearchFilterPaginationRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface ProductService {

    ApiResponse createProduct(ProductRequest productRequest);

    ApiResponse findAllProducts();

    ApiResponse findProductById(Long id);

    ApiResponse removeProduct(Long id);

    ApiResponse updateProduct(Long id, ProductRequest productRequest);

    ApiResponse productList(ProductSearchFilterPaginationRequest request);
}
