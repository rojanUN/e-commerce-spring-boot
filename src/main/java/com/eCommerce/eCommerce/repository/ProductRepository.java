package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    void deleteByCategoryId(Long categoryId);

}
