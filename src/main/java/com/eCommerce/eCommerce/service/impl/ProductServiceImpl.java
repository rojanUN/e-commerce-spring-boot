package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.ProductRequest;
import com.eCommerce.eCommerce.dto.response.ProductResponse;
import com.eCommerce.eCommerce.entity.Category;
import com.eCommerce.eCommerce.entity.Product;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.CategoryRepository;
import com.eCommerce.eCommerce.repository.ProductRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse createProduct(ProductRequest productRequest, Long adminId) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new EcommerceException("CAT001"));

        Product product = new Product();
        return getApiResponse(productRequest, category, product);
    }

    //Helper method
    private ApiResponse getApiResponse(ProductRequest productRequest, Category category, Product product) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setPrice(productRequest.getPrice());
        product.setCategory(category);
        productRepository.save(product);

        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        return ResponseBuilder.buildSuccessResponse(productResponse);
    }

    @Override
    public ApiResponse findAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = products.stream()
                .map(product -> {
                    ProductResponse response = modelMapper.map(product, ProductResponse.class);
                    if (product.getCategory() != null) {
                        response.setCategoryName(product.getCategory().getName());
                    }
                    return response;
                })
                .toList();
        return ResponseBuilder.buildSuccessResponse(productResponses);
    }

    @Override
    public ApiResponse findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EcommerceException("PRO001"));
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        return ResponseBuilder.buildSuccessResponse(productResponse);

    }

    @Override
    public ApiResponse removeProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EcommerceException("PRO001");
        }

        try {
            productRepository.deleteById(id);
            return ResponseBuilder.buildSuccessResponse("message.product.deleted.success");
        } catch (Exception e) {
            throw new EcommerceException("PRO002");
        }
    }

    @Override
    @Transactional
    public ApiResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EcommerceException("PRO001"));

        Category category = productRequest.getCategoryId() != null ?
                categoryRepository.findById(productRequest.getCategoryId())
                        .orElseThrow(() -> new EcommerceException("CAT001")) :
                product.getCategory();

        return getApiResponse(productRequest, category, product);
    }

}
