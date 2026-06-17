package com.learn.product_service.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(name = "app.audit.enabled",havingValue = "true",matchIfMissing =false)
public class AuditAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AuditService auditService(){
        System.out.println("AuditService auto-configured!");
        return new AuditService();
    }
}
