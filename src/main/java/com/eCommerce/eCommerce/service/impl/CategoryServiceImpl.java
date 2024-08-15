package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.dto.response.CategoryResponse;
import com.eCommerce.eCommerce.entity.Category;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.CategoryRepository;
import com.eCommerce.eCommerce.repository.ProductRepository;
import com.eCommerce.eCommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createCategory(CategoryRequest categoryRequest) {
        log.info("Creating category with name: {}", categoryRequest.getName());

        if (categoryRepository.existsByName(categoryRequest.getName().toLowerCase())) {
            throw new EcommerceException("CAT003", HttpStatus.CONFLICT);
        }
        Category category = new Category();
        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);

        CategoryResponse categoryResponse = new CategoryResponse();
        modelMapper.map(category, categoryResponse);

        log.info("Category created successfully with ID: {}", category.getId());
        return ResponseBuilder.buildSuccessResponse(categoryResponse, "message.category.created.success");
    }

    @Override
    public ApiResponse findCategories() {
        log.info("Fetching all categories");

        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();

        log.info("Fetched {} categories", categoryResponses.size());
        return ResponseBuilder.buildSuccessResponse(categoryResponses);
    }

    @Override
    public ApiResponse deleteCategoryById(Long id) {
        log.info("Deleting category with ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            log.error("Category with ID: {} not found", id);
            throw new EcommerceException("CAT001", HttpStatus.NOT_FOUND);
        }

        try {
            productRepository.deleteByCategoryId(id);
            categoryRepository.deleteById(id);
            log.info("Category with ID: {} deleted successfully", id);
            return ResponseBuilder.buildSuccessResponse("message.category.deleted.success");
        } catch (Exception e) {
            log.error("Error deleting category with ID: {}", id, e);
            throw new EcommerceException("CAT002");
        }
    }

    @Override
    public ApiResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        log.info("Updating category with ID: {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category with ID: {} not found", id);
            return new EcommerceException("CAT001", HttpStatus.NOT_FOUND);
        });

        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);

        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

        log.info("Category with ID: {} updated successfully", id);
        return ResponseBuilder.buildSuccessResponse(categoryResponse, "message.category.updated.success");
    }
}
