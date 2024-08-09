package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    @Modifying
    @Query("UPDATE WishListItem w SET w.softDeleted = true WHERE w.product.id = :productId")
    void softDeleteByProductId(@Param("productId") Long productId);

}
