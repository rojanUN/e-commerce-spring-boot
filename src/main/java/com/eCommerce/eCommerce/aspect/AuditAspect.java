package com.eCommerce.eCommerce.aspect;

import com.eCommerce.eCommerce.dto.CategoryRequest;
import com.eCommerce.eCommerce.dto.LoginUserDto;
import com.eCommerce.eCommerce.dto.RegisterUserDto;
import com.eCommerce.eCommerce.entity.AuditLog;
import com.eCommerce.eCommerce.repository.AuditLogRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@AllArgsConstructor
@Component
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @Before("execution(* com.eCommerce.eCommerce.service.impl.AuthenticationServiceImpl.signup(..)) && args(input)")
    public void logSignupActivity(RegisterUserDto input) {
        String email = input.getEmail();
        log.info("Auditing signup for email: {}", email);

        AuditLog log = new AuditLog();
        log.setUsername(email);
        log.setAction("User Signup");
        log.setTimestamp(new Date());
        log.setResource("User");
        log.setDetails("User signed up with email: " + email);

        auditLogRepository.save(log);
    }

    @Before("execution(* com.eCommerce.eCommerce.service.impl.AuthenticationServiceImpl.authenticate(..)) && args(input)")
    public void logLoginActivity(LoginUserDto input) {
        String email = input.getEmail();
        log.info("Auditing login for email: {}", email);

        AuditLog log = new AuditLog();
        log.setUsername(email);
        log.setAction("User Login");
        log.setTimestamp(new Date());
        log.setResource("User");
        log.setDetails("User logged in with email: " + email);

        auditLogRepository.save(log);
    }

    @Transactional
    @Before("execution(* com.eCommerce.eCommerce.service.impl.CategoryServiceImpl.createCategory(..)) && args(categoryRequest)")
    public void logCategoryCreation(JoinPoint joinPoint, CategoryRequest categoryRequest) {
        String methodName = joinPoint.getSignature().getName();
        String categoryName = categoryRequest.getName();
        String action = "Category Created";

        log.info("Auditing {} method call with category name: {}", methodName, categoryName);

        AuditLog log = new AuditLog();
        log.setUsername("SuperAdmin");
        log.setAction(action);
        log.setTimestamp(new Date());
        log.setResource("Category");
        log.setDetails("Created category with name: " + categoryName);

        auditLogRepository.save(log);
    }

    @Before("execution(* com.eCommerce.eCommerce.service.impl.CategoryServiceImpl.deleteCategoryById(..)) && args(id)")
    public void logCategoryDeletion(Long id) {
        log.info("Auditing category deletion with ID: {}", id);

        AuditLog log = new AuditLog();
        log.setUsername("SuperAdmin");
        log.setAction("Category Deleted");
        log.setTimestamp(new Date());
        log.setResource("Category");
        log.setDetails("Deleted category with ID: " + id);

        auditLogRepository.save(log);
    }


}
