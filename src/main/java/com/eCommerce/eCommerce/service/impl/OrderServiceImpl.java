package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.OrderItemRequest;
import com.eCommerce.eCommerce.dto.OrderRequest;
import com.eCommerce.eCommerce.dto.response.OrderResponse;
import com.eCommerce.eCommerce.entity.*;
import com.eCommerce.eCommerce.enums.OrderStatus;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.*;
import com.eCommerce.eCommerce.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public ApiResponse createOrder(OrderRequest orderRequest, Long userId) {

        Address address = addressRepository.findById(orderRequest.getShippingAddressId())
                .orElseThrow(() -> new EcommerceException("ADR001"));
        if (!address.getUser().getId().equals(userId)) {
            throw new EcommerceException("ADR002");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderRequest.getPaymentMethodId())
                .orElseThrow(() -> new EcommerceException("PMT001"));
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new EcommerceException("PMT001");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EcommerceException("PRD001"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setUnitPrice(itemTotalPrice);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(itemTotalPrice);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.created.success");
    }
}

