package com.eCommerce.eCommerce;

import com.eCommerce.eCommerce.util.AESEncryptionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class ECommerceApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(Base64.getEncoder().encodeToString(AESEncryptionUtil.generateKey().getEncoded()));
        SpringApplication.run(ECommerceApplication.class, args);
    }

}
