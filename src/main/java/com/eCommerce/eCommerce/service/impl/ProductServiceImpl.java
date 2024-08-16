package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.ProductRequest;
import com.eCommerce.eCommerce.dto.ProductSearchFilterPaginationRequest;
import com.eCommerce.eCommerce.dto.response.DataPaginationResponse;
import com.eCommerce.eCommerce.dto.response.ProductResponse;
import com.eCommerce.eCommerce.entity.Category;
import com.eCommerce.eCommerce.entity.Product;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.*;
import com.eCommerce.eCommerce.service.NotificationService;
import com.eCommerce.eCommerce.service.ProductService;
import com.eCommerce.eCommerce.service.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final WishListItemRepository wishListItemRepository;

    private final ReviewRepository reviewRepository;

    private final OrderItemRepository orderItemRepository;

    private final NotificationService notificationService;

    @Override
    public ApiResponse createProduct(ProductRequest productRequest) {
        log.info("Creating product with name: {}", productRequest.getName());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category with ID: {} not found", productRequest.getCategoryId());
                    return new EcommerceException("CAT001", HttpStatus.NOT_FOUND);
                });

        Product product = new Product();
        return getApiResponse(productRequest, category, product);
    }

    @Override
    public void sendNotification(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID: {} not found", id);
            return new EcommerceException("PRO001", HttpStatus.NOT_FOUND);
        });

        notificationService.notifyAdminLowStock(product);
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
        log.info("Product created successfully with ID: {}", product.getId());
        return ResponseBuilder.buildSuccessResponse(productResponse);
    }

    @Override
    public ApiResponse findAllProducts() {
        log.info("Fetching all products");

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

        log.info("Found {} products", productResponses.size());
        return ResponseBuilder.buildSuccessResponse(productResponses);
    }

    @Override
    public ApiResponse findProductById(Long id) {
        log.info("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID: {} not found", id);
            return new EcommerceException("PRO001", HttpStatus.NOT_FOUND);
        });

        Double averageRating = reviewRepository.calculateAverageRatingByProductId(id);
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setAverageRating(averageRating);
        return ResponseBuilder.buildSuccessResponse(productResponse);
    }

    @Override
    @Transactional
    public ApiResponse removeProduct(Long id) {
        log.info("Removing product with ID: {}", id);

        if (!productRepository.existsById(id)) {
            log.error("Product with ID: {} does not exist", id);
            throw new EcommerceException("PRO001", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            reviewRepository.softDeleteByProductId(id);
            wishListItemRepository.softDeleteByProductId(id);
            orderItemRepository.softDeleteByProductId(id);
//            productRepository.deleteById(id);
            productRepository.softDeleteById(id);

            log.info("Product with ID: {} removed successfully", id);
            return ResponseBuilder.buildSuccessResponse("message.product.deleted.success");
        } catch (Exception e) {
            log.error("Error removing product with ID: {}", id, e);
            throw new EcommerceException("PRO002");
        }
    }

    @Override
    @Transactional
    public ApiResponse updateProduct(Long id, ProductRequest productRequest) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID: {} not found", id);
                    return new EcommerceException("PRO001", HttpStatus.NOT_FOUND);
                });

        Category category = productRequest.getCategoryId() != null ?
                categoryRepository.findById(productRequest.getCategoryId())
                        .orElseThrow(() -> {
                            log.error("Category with ID: {} not found", productRequest.getCategoryId());
                            return new EcommerceException("CAT001", HttpStatus.NOT_FOUND);
                        }) :
                product.getCategory();

        return getApiResponse(productRequest, category, product);
    }

    @Override
    public ApiResponse productList(ProductSearchFilterPaginationRequest request) {
        log.info("Fetching Paginated Product response");
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(),
                Sort.by(Objects.equals(request.getDirection(), "asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC, request.getSortBy() == null
                        ? "createdAt" : request.getSortBy()
                )
        );

        Specification<Product> specification = ProductSpecification.productFilterSearch(request);
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        List<Product> productList = productPage.getContent();

        List<ProductResponse> productResponseList =
                productList.stream()
                        .map(product -> {
                            ProductResponse productResponse = new ModelMapper().map(product, ProductResponse.class);
                            return productResponse;
                        }).toList();
        DataPaginationResponse dataPaginationResponse = DataPaginationResponse.builder()
                .totalElementCount(productPage.getTotalElements())
                .result(productResponseList)
                .build();

        log.info("Product page with {} records found", dataPaginationResponse.getTotalElementCount());
        return ResponseBuilder.buildSuccessResponse(dataPaginationResponse);

    }

}
