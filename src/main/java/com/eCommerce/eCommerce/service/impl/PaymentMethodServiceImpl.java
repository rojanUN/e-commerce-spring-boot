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
import com.eCommerce.eCommerce.util.AESEncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ModelMapper modelMapper;

    @Value("${aes.secret-key}")
    private String secretKey;

    @Override
    @Transactional
    public ApiResponse addPaymentMethod(Long userId, PaymentMethodRequest paymentMethodRequest) throws Exception {
        log.info("Adding payment method for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", userId);
                    return new EcommerceException("USR001", HttpStatus.NOT_FOUND);
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

        try {
            String encryptedAccountNumber = AESEncryptionUtil.encrypt(paymentMethodRequest.getAccountNumber(), secretKey);
            paymentMethod.setAccountNumber(encryptedAccountNumber);
        } catch (Exception e) {
            log.error("Failed to encrypt account number for user ID: {}", userId, e);
            throw new EcommerceException("ENC001", HttpStatus.FAILED_DEPENDENCY);
        }

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
                    return new EcommerceException("PMT001", HttpStatus.NOT_FOUND);
                });

        if (!paymentMethod.getUser().getId().equals(userId)) {
            log.error("Payment method ID: {} does not belong to user ID: {}", paymentMethodId, userId);
            throw new EcommerceException("PMT002", HttpStatus.FORBIDDEN);
        }

        paymentMethodRepository.delete(paymentMethod);
        log.info("Payment method ID: {} removed successfully for user ID: {}", paymentMethodId, userId);
        return ResponseBuilder.buildSuccessResponse("message.payment.method.deleted.success");
    }

    @Override
    @Transactional
    public ApiResponse updatePaymentMethod(Long userId, Long paymentMethodId, PaymentMethodRequest paymentMethodRequest) throws Exception {
        log.info("Updating payment method ID: {} for user ID: {}", paymentMethodId, userId);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> {
                    log.error("Payment method with ID: {} not found", paymentMethodId);
                    return new EcommerceException("PMT001", HttpStatus.NOT_FOUND);
                });

        if (!paymentMethod.getUser().getId().equals(userId)) {
            log.error("Payment method ID: {} does not belong to user ID: {}", paymentMethodId, userId);
            throw new EcommerceException("PMT001", HttpStatus.FORBIDDEN);
        }

        if (paymentMethodRequest.getIsDefault()) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultMethod -> {
                log.info("Updating default payment method for user ID: {}", userId);
                defaultMethod.setIsDefault(false);
                paymentMethodRepository.save(defaultMethod);
            });
        }

        modelMapper.map(paymentMethodRequest, paymentMethod);
        try {
            String encryptedAccountNumber = AESEncryptionUtil.encrypt(paymentMethodRequest.getAccountNumber(), secretKey);
            paymentMethod.setAccountNumber(encryptedAccountNumber);
        } catch (Exception e) {
            log.error("Failed to encrypt account number for user ID: {}", userId, e);
            throw new EcommerceException("Encryption failed", e);
        }
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
                .map(paymentMethod -> {
                    PaymentMethodResponse response = modelMapper.map(paymentMethod, PaymentMethodResponse.class);
                    try {
                        String decryptedAccountNumber = AESEncryptionUtil.decrypt(paymentMethod.getAccountNumber(), secretKey);
                        response.setAccountNumber(decryptedAccountNumber);
                    } catch (Exception e) {
                        log.error("Failed to decrypt account number for payment method ID: {}", paymentMethod.getId(), e);
                        throw new EcommerceException("Failed to decrypt account number", e);
                    }
                    return response;
                })
                .toList();

        log.info("Found {} payment methods for user ID: {}", paymentMethodResponses.size(), userId);
        return ResponseBuilder.buildSuccessResponse(paymentMethodResponses);
    }
}
