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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ApiResponse addPaymentMethod(Long userId, PaymentMethodRequest paymentMethodRequest) {

        User user = userRepository.findById(userId).
                orElseThrow(() -> new EcommerceException("USR001"));

        if (paymentMethodRequest.getIsDefault()) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultAddress -> {
                defaultAddress.setIsDefault(false);
                paymentMethodRepository.save(defaultAddress);
            });
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        modelMapper.map(paymentMethodRequest, paymentMethod);
        paymentMethod.setUser(user);
        paymentMethodRepository.save(paymentMethod);
        return ResponseBuilder.buildSuccessResponse("message.payment.method.created.success");

    }

    @Override
    public ApiResponse removePaymentMethod(Long userId, Long paymentMethodId) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EcommerceException("PMT001"));
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new EcommerceException("PMT002");
        }
        paymentMethodRepository.delete(paymentMethod);
        return ResponseBuilder.buildSuccessResponse("message.payment.method.deleted.success");

    }

    @Override
    @Transactional
    public ApiResponse updatePaymentMethod(Long userId, Long paymentMethodId, PaymentMethodRequest paymentMethodRequest) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EcommerceException("PMT001"));

        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new EcommerceException("PMT001");
        }

        if (paymentMethodRequest.getIsDefault()) {
            paymentMethodRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultAddress -> {
                defaultAddress.setIsDefault(false);
                paymentMethodRepository.save(defaultAddress);
            });
        }
        modelMapper.map(paymentMethodRequest, paymentMethod);
        paymentMethodRepository.save(paymentMethod);

        PaymentMethodResponse paymentMethodResponse = modelMapper.map(paymentMethod, PaymentMethodResponse.class);
        return ResponseBuilder.buildSuccessResponse(paymentMethodResponse, "message.payment.method.updated.success");

    }

    @Override
    public ApiResponse findAllPaymentMethodByUserId(Long userId) {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByUserId(userId);
        if (paymentMethods.isEmpty()) {
            return ResponseBuilder.buildSuccessResponse("message.payment.method.empty");
        }

        List<PaymentMethodResponse> paymentMethodResponses = paymentMethods.stream()
                .map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodResponse.class))
                .toList();

        return ResponseBuilder.buildSuccessResponse(paymentMethodResponses);
    }

}
