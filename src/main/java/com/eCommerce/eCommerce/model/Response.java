package com.eCommerce.eCommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {

    public boolean success;
    @JsonIgnore
    public HttpStatus httpStatus;
    public String code;
    public String message;

}

