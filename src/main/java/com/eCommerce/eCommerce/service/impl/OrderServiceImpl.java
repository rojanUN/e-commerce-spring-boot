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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public ApiResponse createOrder(OrderRequest orderRequest, Long userId) {
        log.info("Creating order for user ID: {}", userId);

        if (CollectionUtils.isEmpty(orderRequest.getOrderItems())) {
            log.error("Order creation failed: Order items are empty");
            throw new EcommerceException("ODR001", HttpStatus.CONFLICT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", userId);
                    return new EcommerceException("USR001", HttpStatus.NOT_FOUND);
                });

        Address address = (orderRequest.getShippingAddressId() == null) ?
                addressRepository.findByUserIdAndIsDefaultTrue(userId)
                        .orElseThrow(() -> {
                            log.error("Default shipping address for user ID: {} not found", userId);
                            return new EcommerceException("ADR003", HttpStatus.NOT_FOUND);
                        }) :
                addressRepository.findById(orderRequest.getShippingAddressId())
                        .orElseThrow(() -> {
                            log.error("Shipping address with ID: {} not found", orderRequest.getShippingAddressId());
                            return new EcommerceException("ADR001", HttpStatus.NOT_FOUND);
                        });

        if (!address.getUser().getId().equals(userId)) {
            log.error("Address ID: {} does not belong to user ID: {}", orderRequest.getShippingAddressId(), userId);
            throw new EcommerceException("ADR002", HttpStatus.CONFLICT);
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderRequest.getPaymentMethodId())
                .orElseThrow(() -> {
                    log.error("Payment method with ID: {} not found", orderRequest.getPaymentMethodId());
                    return new EcommerceException("PMT001", HttpStatus.NOT_FOUND);
                });

        if (!paymentMethod.getUser().getId().equals(userId)) {
            log.error("Payment method ID: {} does not belong to user ID: {}", orderRequest.getPaymentMethodId(), userId);
            throw new EcommerceException("PMT001", HttpStatus.FORBIDDEN);
        }

        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(address);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> {
                        log.error("Product with ID: {} not found", itemRequest.getProductId());
                        return new EcommerceException("PRD001", HttpStatus.NOT_FOUND);
                    });

            if (itemRequest.getQuantity() > product.getStock()) {
                log.error("Insufficient stock for product ID: {}. Requested: {}, Available: {}",
                        itemRequest.getProductId(), itemRequest.getQuantity(), product.getStock());
                throw new EcommerceException("PRO003", HttpStatus.CONFLICT);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setItemPrice(itemTotalPrice);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(itemTotalPrice);

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        log.info("Order created successfully with ID: {}", order.getId());

        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.created.success");
    }


    @Override
    public ApiResponse findOrders(Long userId) {
        log.info("Finding orders for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", userId);
                    return new EcommerceException("USR001", HttpStatus.NOT_FOUND);
                });

        List<Order> orders = orderRepository.findOrdersByUserId(user.getId());

        List<OrderResponse> orderResponses = orders.stream().distinct()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();

        log.info("Found {} orders for user ID: {}", orderResponses.size(), userId);
        return ResponseBuilder.buildSuccessResponse(orderResponses, "message.order.fetch.success");
    }

    @Override
    public ApiResponse cancelOrder(Long userId, Long orderId) {
        log.info("Cancelling order ID: {} for user ID: {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID: {} not found", orderId);
                    return new EcommerceException("ODR001", HttpStatus.NOT_FOUND);
                });

        if (!order.getUser().getId().equals(userId)) {
            log.error("User ID: {} does not match the owner of order ID: {}", userId, orderId);
            throw new EcommerceException("ODR003", HttpStatus.FORBIDDEN);
        }

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            log.error("Order ID: {} is already SHIPPED", orderId);
            throw new EcommerceException("ODR004", HttpStatus.CONFLICT);
        }

        order.setStatus(OrderStatus.CANCELLED);
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderId(orderId);
        int quantity = orderItem.getQuantity();
        Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(() -> new EcommerceException("PRO001"));
        product.setStock(product.getStock() + quantity);
        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        log.info("Order ID: {} cancelled successfully", orderId);
        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.cancel.success");
    }

    @Override
    public ApiResponse completeOrder(Long orderId) {
        log.info("Completing order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID: {} not found", orderId);
                    return new EcommerceException("ODR001", HttpStatus.NOT_FOUND);
                });

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            log.error("Order ID: {} is not in PENDING status", orderId);
            throw new EcommerceException("ODR006", HttpStatus.CONFLICT);
        }

        order.setStatus(OrderStatus.COMPLETED);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        log.info("Order ID: {} completed successfully", orderId);
        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.complete.success");
    }
}
