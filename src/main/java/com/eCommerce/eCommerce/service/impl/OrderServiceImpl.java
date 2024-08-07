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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
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

        if (CollectionUtils.isEmpty(orderRequest.getOrderItems())) {
            throw new EcommerceException("ODR001");
        }

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
            orderItem.setItemPrice(itemTotalPrice);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(itemTotalPrice);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.created.success");
    }

    @Override
    public ApiResponse findOrders(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EcommerceException("USR001"));

        List<Order> orders = orderRepository.findOrdersByUserId(user.getId());

        List<OrderResponse> orderResponses = orders.stream().distinct().map(order -> modelMapper.map(order, OrderResponse.class)).toList();

        return ResponseBuilder.buildSuccessResponse(orderResponses, "message.order.fetch.success");
    }

    @Override
    public ApiResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EcommerceException("ODR001"));

        if (!order.getUser().getId().equals(userId)){
            throw new EcommerceException("ODR003");
        }

        if (!order.getStatus().equals(OrderStatus.PENDING)){
            throw new EcommerceException("ODR004");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.cancel.success");
    }

    @Override
    public ApiResponse completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EcommerceException("ODR001"));

        if (!order.getStatus().equals(OrderStatus.PENDING)){
            throw new EcommerceException("ODR006");
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        return ResponseBuilder.buildSuccessResponse(orderResponse, "message.order.complete.success");
    }


}

