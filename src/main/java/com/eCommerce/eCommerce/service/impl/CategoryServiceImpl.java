package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ServiceResponseBuilder;
import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.dto.response.CategoryResponse;
import com.eCommerce.eCommerce.entity.Category;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.CategoryRepository;
import com.eCommerce.eCommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = new CategoryResponse();
        modelMapper.map(categoryRequest, categoryResponse);
        return ResponseEntity.ok(categoryResponse);
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> findCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setId(category.getId());
                    response.setName(category.getName());
                    response.setDescription(category.getDescription());
                    return response;
                })
                .toList();
        return ResponseEntity.ok(categoryResponses);
    }

    @Override
    public ApiResponse deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EcommerceException("CAT001");
        }

        try {
            categoryRepository.deleteById(id);
            return ServiceResponseBuilder.buildSuccessResponse("message.category.deleted.success");
        } catch (Exception e) {
            throw new EcommerceException("CAT002");
        }
    }
}
