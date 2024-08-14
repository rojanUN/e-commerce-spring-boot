package com.eCommerce.eCommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wishListItem")
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishList_id", nullable = false)
    private WishList wishList;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private boolean softDeleted = false;

}
