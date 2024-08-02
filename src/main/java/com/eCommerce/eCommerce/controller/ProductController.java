package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ProductRequest;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ApiResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping("/find-all")
    public ApiResponse findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}/find")
    public ApiResponse findProductById(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/remove")
    public ApiResponse removeProductById(@PathVariable Long id){
        return productService.removeProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update")
    public ApiResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
        return productService.updateProduct(id, productRequest);
    }

}
