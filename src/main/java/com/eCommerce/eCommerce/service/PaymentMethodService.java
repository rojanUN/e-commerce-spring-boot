package com.eCommerce.eCommerce.service;

import com.eCommerce.eCommerce.dto.PaymentMethodRequest;
import com.eCommerce.eCommerce.model.ApiResponse;

public interface PaymentMethodService {

    ApiResponse addPaymentMethod(Long userId, PaymentMethodRequest paymentMethodRequest) throws Exception;

    ApiResponse removePaymentMethod(Long userId, Long paymentMethodId);

    ApiResponse updatePaymentMethod(Long userId, Long paymentMethodId, PaymentMethodRequest paymentMethodRequest) throws Exception;

    ApiResponse findAllPaymentMethodByUserId(Long userId);

}
