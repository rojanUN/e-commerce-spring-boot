package com.eCommerce.eCommerce.repository;

import com.eCommerce.eCommerce.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
}
