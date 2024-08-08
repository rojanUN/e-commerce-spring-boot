package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {

    @Transactional
    void deleteByCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Product p SET p.softDeleted = true WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

}
