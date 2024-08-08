package com.eCommerce.eCommerce.builder;

import com.eCommerce.eCommerce.model.ApiResponse;

public class ResponseBuilder {

    public static ApiResponse buildSuccessResponse(Object o) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(Boolean.TRUE);
        response.setData(o);
        return response;
    }

    public static ApiResponse buildSuccessResponse(String message) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(Boolean.TRUE);
        response.setMessage(message);
        return response;
    }

    public static ApiResponse buildSuccessResponse(Object o, String message) {
        ApiResponse response = buildSuccessResponse(o);
        response.setMessage(message);
        return response;
    }

//    public static ApiError buildFailResponse(EcommerceException exception) {
//        ApiError response = new ApiError();
//        response.setSuccess(Boolean.FALSE);
//        response.setCode(exception.getCode());
//        response.setMessage(exception.getMessage());
//        return response;
//
//    }
//
//    public static ApiError buildUnknownFailResponse(Exception exception) {
//        ApiError response = new ApiError();
//        response.setSuccess(Boolean.FALSE);
//        response.setCode("000000");
//        response.setMessage(exception.getMessage());
//        response.setDebugMessage(ExceptionUtil.getStackTraceString(exception));
//        return response;
//
//    }
//
//    public static ApiResponse buildResponse(String message, HttpStatus httpStatus) {
//        ApiResponse response = new ApiResponse();
//        response.setSuccess(Boolean.FALSE);
//        response.setMessage(message);
//        response.setHttpStatus(httpStatus);
//        return response;
//    }
//
//    public static <T> ApiResponse buildResponse(List<?> data) {
//        ApiResponse response = new ApiResponse();
//        response.setData(data);
//        response.setMessage("Success");
//        return response;
//    }
}

