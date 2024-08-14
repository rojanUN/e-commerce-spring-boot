package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.response.AuditLogResponse;
import com.eCommerce.eCommerce.entity.AuditLog;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.AuditLogRepository;
import com.eCommerce.eCommerce.service.AuditLogService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse findAuditLogs() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();

        List<AuditLogResponse> auditLogResponses = auditLogs
                .stream()
                .map(auditLog -> modelMapper.map(auditLog, AuditLogResponse.class))
                .toList();
        return ResponseBuilder.buildSuccessResponse(auditLogResponses);
    }
}
