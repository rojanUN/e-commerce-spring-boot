package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.dto.response.CategoryResponse;
import com.eCommerce.eCommerce.model.ApiError;
import com.eCommerce.eCommerce.model.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<CategoryResponse> createCategory(CategoryRequest categoryRequest);
    ResponseEntity<List<CategoryResponse>> findCategories();
    ApiResponse deleteCategoryById(Long id);
}
