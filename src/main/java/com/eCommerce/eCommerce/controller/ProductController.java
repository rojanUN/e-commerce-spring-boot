package com.eCommerce.eCommerce.controller;

import com.eCommerce.eCommerce.dto.ProductRequest;
import com.eCommerce.eCommerce.dto.ProductSearchFilterPaginationRequest;
import com.eCommerce.eCommerce.model.Response;
import com.eCommerce.eCommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Response> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Response> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}/find")
    public ResponseEntity<Response> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Response> removeProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.removeProduct(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @PostMapping("/list")
    public ResponseEntity<Response> listProduct(@RequestBody ProductSearchFilterPaginationRequest request) {
        return ResponseEntity.ok(productService.productList(request));
    }

}
