package com.eCommerce.eCommerce.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuditLogResponse {

    private Long id;

    private String username;

    private String action;

    private Date timestamp;

    private String resource;

    private String details;
}
