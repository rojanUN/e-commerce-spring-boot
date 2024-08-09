package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Response> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Response> findCategories() {
        return ResponseEntity.ok(categoryService.findCategories());
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategoryById(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Response> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
    }
}
