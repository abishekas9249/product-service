package com.learn.product_service.config;

public class AuditService {
    public void log(String action,String user){
        System.out.println("[AUDIT]"+action+" by "+user);
    }
}
