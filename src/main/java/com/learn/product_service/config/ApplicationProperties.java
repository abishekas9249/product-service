package com.learn.product_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="app.product")
@Data
public class ApplicationProperties {
    private int maxPageSize=10;
    private String defaultCurrency="INR";
    private boolean cacheEnabled=true;
}
