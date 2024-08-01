package com.eCommerce.eCommerce.exceptions;

import com.eCommerce.eCommerce.util.MessageBundle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EcommerceException extends RuntimeException {

    private String code;

    private HttpStatus httpStatus;

    @Setter
    private String debugMessage;

    public EcommerceException(String code) {
        super(MessageBundle.lookup.get(code));
        this.code = code;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        this.debugMessage = "";
    }

    public EcommerceException(String code, List<String> param) {
        super(String.format(MessageBundle.lookup.get(code), String.join(",", param)));
        this.code = code;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        this.debugMessage = "";
    }

    public EcommerceException(String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        this.debugMessage = "";
    }

    public EcommerceException(String code, Throwable throwable) {
        super(throwable.getMessage());
        this.code = code;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        this.debugMessage = ExceptionUtil.getStackTraceString(throwable);
    }

    public EcommerceException(String code, HttpStatus httpStatus) {
        super(MessageBundle.lookup.get(code));
        this.code = code;
        this.httpStatus = httpStatus;
        this.debugMessage = "";
    }

    public EcommerceException(String code, HttpStatus httpStatus, String debugMessage) {
        super(MessageBundle.lookup.get(code));
        this.code = code;
        this.httpStatus = httpStatus;
        this.debugMessage = debugMessage;
    }

}
