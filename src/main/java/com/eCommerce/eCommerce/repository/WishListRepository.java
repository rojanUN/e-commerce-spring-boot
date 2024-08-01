package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Query("SELECT w FROM WishList w WHERE w.user.id = :userId")
    WishList findByUserId(@Param("userId") Long userId);

}
