package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.PaymentMethodRequest;
import com.eCommerce.eCommerce.dto.response.PaymentMethodResponse;
import com.eCommerce.eCommerce.entity.PaymentMethod;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.PaymentMethodRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.PaymentMethodService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ApiResponse addPaymentMethod(Long userId, PaymentMethodRequest paymentMethodRequest) {
        log.info("Adding payment method for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", userId);
                    return new EcommerceException("USR001");
                });

        if (paymentMethodRequest.getIsDefault()) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultMethod -> {
                log.info("Updating default payment method for user ID: {}", userId);
                defaultMethod.setIsDefault(false);
                paymentMethodRepository.save(defaultMethod);
            });
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        modelMapper.map(paymentMethodRequest, paymentMethod);
        paymentMethod.setUser(user);
        paymentMethodRepository.save(paymentMethod);

        log.info("Payment method created successfully for user ID: {}", userId);
        return ResponseBuilder.buildSuccessResponse("message.payment.method.created.success");
    }

    @Override
    public ApiResponse removePaymentMethod(Long userId, Long paymentMethodId) {
        log.info("Removing payment method ID: {} for user ID: {}", paymentMethodId, userId);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> {
                    log.error("Payment method with ID: {} not found", paymentMethodId);
                    return new EcommerceException("PMT001");
                });

        if (!paymentMethod.getUser().getId().equals(userId)) {
            log.error("Payment method ID: {} does not belong to user ID: {}", paymentMethodId, userId);
            throw new EcommerceException("PMT002");
        }

        paymentMethodRepository.delete(paymentMethod);
        log.info("Payment method ID: {} removed successfully for user ID: {}", paymentMethodId, userId);
        return ResponseBuilder.buildSuccessResponse("message.payment.method.deleted.success");
    }

    @Override
    @Transactional
    public ApiResponse updatePaymentMethod(Long userId, Long paymentMethodId, PaymentMethodRequest paymentMethodRequest) {
        log.info("Updating payment method ID: {} for user ID: {}", paymentMethodId, userId);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> {
                    log.error("Payment method with ID: {} not found", paymentMethodId);
                    return new EcommerceException("PMT001");
                });

        if (!paymentMethod.getUser().getId().equals(userId)) {
            log.error("Payment method ID: {} does not belong to user ID: {}", paymentMethodId, userId);
            throw new EcommerceException("PMT001");
        }

        if (paymentMethodRequest.getIsDefault()) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultMethod -> {
                log.info("Updating default payment method for user ID: {}", userId);
                defaultMethod.setIsDefault(false);
                paymentMethodRepository.save(defaultMethod);
            });
        }

        modelMapper.map(paymentMethodRequest, paymentMethod);
        paymentMethodRepository.save(paymentMethod);

        PaymentMethodResponse paymentMethodResponse = modelMapper.map(paymentMethod, PaymentMethodResponse.class);
        log.info("Payment method ID: {} updated successfully for user ID: {}", paymentMethodId, userId);
        return ResponseBuilder.buildSuccessResponse(paymentMethodResponse, "message.payment.method.updated.success");
    }

    @Override
    public ApiResponse findAllPaymentMethodByUserId(Long userId) {
        log.info("Finding all payment methods for user ID: {}", userId);

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserId(userId);
        if (paymentMethods.isEmpty()) {
            log.info("No payment methods found for user ID: {}", userId);
            return ResponseBuilder.buildSuccessResponse("message.payment.method.empty");
        }

        List<PaymentMethodResponse> paymentMethodResponses = paymentMethods.stream()
                .map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodResponse.class))
                .toList();

        log.info("Found {} payment methods for user ID: {}", paymentMethodResponses.size(), userId);
        return ResponseBuilder.buildSuccessResponse(paymentMethodResponses);
    }
}
