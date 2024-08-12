package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.entity.Product;
import com.eCommerce.eCommerce.service.EmailService;
import com.eCommerce.eCommerce.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;

    @Override
    public void notifyAdminLowStock(Product product) {
        String subject = "Low Stock Alert: " + product.getName();
        String message = "The stock for product " + product.getName() +
                " (ID: " + product.getId() + ") is running low. " +
                "Current stock: " + product.getStock();

        emailService.sendEmail("rojanpanta777@gmail.com", subject, message);
    }

}
