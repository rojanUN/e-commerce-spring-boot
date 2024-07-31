package com.eCommerce.eCommerce.util;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String key, Object... params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    public String getError(String key,Object... params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}
