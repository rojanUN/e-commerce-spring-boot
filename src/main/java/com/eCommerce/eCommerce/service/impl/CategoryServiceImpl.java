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
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;


    @Override
    public ApiResponse createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = new CategoryResponse();
        modelMapper.map(categoryRequest, categoryResponse);
        return ServiceResponseBuilder.buildSuccessResponse(categoryResponse);
    }

    @Override
    public ApiResponse findCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();

        return ServiceResponseBuilder.buildSuccessResponse(categoryResponses);
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

    @Override
    public ApiResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EcommerceException("CAT001"));
        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        return ServiceResponseBuilder.buildSuccessResponse(categoryResponse);
    }


}
