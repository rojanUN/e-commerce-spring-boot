package com.eCommerce.eCommerce.service.specification;

import com.eCommerce.eCommerce.dto.ProductSearchFilterPaginationRequest;
import com.eCommerce.eCommerce.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductSpecification {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Specification<Product> productFilterSearch(ProductSearchFilterPaginationRequest request) {
        return (root, query, criteriaBuilder) -> {
            Predicate finalPredicate = criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(request.getSearchText())) {
                Predicate searchTextPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern(request.getSearchText())),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern(request.getSearchText()))
                );
                finalPredicate = criteriaBuilder.and(finalPredicate, searchTextPredicate);
            }

            if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty() && !request.getCategoryIds().contains(0L)) {
                Predicate categoryPredicate = root.get("category").get("id").in(request.getCategoryIds());
                finalPredicate = criteriaBuilder.and(finalPredicate, categoryPredicate);
            }

            if (request.getPriceFloor() != null) {
                Predicate priceFloorPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getPriceFloor());
                finalPredicate = criteriaBuilder.and(finalPredicate, priceFloorPredicate);

            }

            if ((request.getPriceCeiling() != null)) {
                Predicate priceCeilingPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getPriceCeiling());
                finalPredicate = criteriaBuilder.and(finalPredicate, priceCeilingPredicate);
            }

            if (request.getCreatedDateFrom() != null && !request.getCreatedDateFrom().isEmpty() && request.getCreatedDateTo() != null && !request.getCreatedDateTo().isEmpty()) {
                LocalDateTime from = LocalDateTime.parse(request.getCreatedDateFrom(), formatter);
                LocalDateTime to = LocalDateTime.parse(request.getCreatedDateTo(), formatter);
                Predicate createdDatePredicate = criteriaBuilder.between(root.get("createdAt"), from, to);
                finalPredicate = criteriaBuilder.and(finalPredicate, createdDatePredicate);
            }

            return finalPredicate;
        };
    }

    private static String likePattern(String value) {
        return "%" + value + "%";
    }


}
