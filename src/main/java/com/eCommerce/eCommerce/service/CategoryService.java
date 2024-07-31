package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface CategoryService {
    ApiResponse createCategory(CategoryRequest categoryRequest);

    ApiResponse findCategories();

    ApiResponse deleteCategoryById(Long id);

    ApiResponse updateCategory(Long id, CategoryRequest categoryRequest);
}
