package com.eCommerce.eCommerce.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class ApiError extends Response implements Serializable {

    private String debugMessage;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

