package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.entity.Product;

public interface NotificationService {

    void notifyAdminLowStock(Product product);

}
