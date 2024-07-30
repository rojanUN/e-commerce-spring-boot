package com.eCommerce.eCommerce.entity;

import com.eCommerce.eCommerce.enums.PaymentType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "paymentMethods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Date expiryDate;

    private Boolean isDefault;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
