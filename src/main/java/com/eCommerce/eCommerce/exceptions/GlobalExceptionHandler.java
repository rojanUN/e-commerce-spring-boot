package com.eCommerce.eCommerce.exceptions;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.model.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EcommerceException.class)
    public ResponseEntity<Response> handleEcommerceException(EcommerceException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ResponseBuilder.buildFailResponse(e));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseBuilder.buildFailResponse(e));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Response> handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseBuilder.buildResponse("Duplicate key error" + e.getMessage(), HttpStatus.CONFLICT));
    }


}
