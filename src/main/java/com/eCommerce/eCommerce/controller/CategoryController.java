package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.dto.response.CategoryResponse;
import com.eCommerce.eCommerce.model.ApiError;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse createCategory(@RequestBody CategoryRequest categoryRequest){
        return categoryService.createCategory(categoryRequest);
    }

    @GetMapping("/find-all")
    public ApiResponse findCategories(){
        return categoryService.findCategories();
    }

    @DeleteMapping("/{id}/remove")
    public ApiResponse deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);
    }

    @PutMapping("/{id}/update")
    public ApiResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategory(id, categoryRequest);
    }
}
