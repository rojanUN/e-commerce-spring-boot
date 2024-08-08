package com.eCommerce.eCommerce.specification;

import com.eCommerce.eCommerce.dto.ProductSearchFilterPaginationRequest;
import com.eCommerce.eCommerce.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.format.DateTimeFormatter;

public class ProductSpecification {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Specification<Product> productFilterSearch(ProductSearchFilterPaginationRequest request){
        return(root, query, criteriaBuilder) -> {
            Predicate finalPredicate =criteriaBuilder.conjunction();
            if (StringUtils.isNotBlank(request.getSearchText())){
                Predicate searchTextPredicate =criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern(request.getSearchText())),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern(request.getSearchText()))
                );
                finalPredicate =criteriaBuilder.and(finalPredicate, searchTextPredicate);
            }

            return  finalPredicate;
        };
    }

    private static String likePattern(String value) {
        return "%" + value + "%";
    }


}
