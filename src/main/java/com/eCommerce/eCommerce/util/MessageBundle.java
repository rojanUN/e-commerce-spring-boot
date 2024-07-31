package com.eCommerce.eCommerce.util;

import com.eCommerce.eCommerce.model.ApiError;
import com.eCommerce.eCommerce.model.Codes;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class MessageBundle {
    public static final Map<String, String> lookup = new HashMap<>();
    private static final String ERROR_FILE = "errors";
    private static final String PROPERTIES = "properties";
    private static final String SEPARATOR = "_";
    private static final String DOT = ".";

    @Bean
    public Codes getCodeLookups() {
        return code -> new ApiError(code, lookup.getOrDefault(code, AppConstant.CODE_NOT_REGISTERED_MESSAGE));
    }
    @PostConstruct
    public void initialize() {
        try {
            String lang = LocaleContextHolder.getLocale().getLanguage();
            Properties properties = readProperties(ERROR_FILE + (lang.equals("en") ? "" : (SEPARATOR + lang)) + DOT + PROPERTIES);
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                lookup.put(key, value);
            }
        } catch (IOException e) {
            // Handle exception appropriately
            log.error("Error: " + e.getMessage());
        }
    }

    public static Properties readProperties(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(fileName);
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }
}



