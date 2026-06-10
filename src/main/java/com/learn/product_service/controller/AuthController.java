package com.learn.product_service.controller;

import com.learn.product_service.dto.LoginRequest;
import com.learn.product_service.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest req){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUserName(),req.getPassword()));
        String token=jwtService.generateToken(req.getUserName());
        return ResponseEntity.ok(Map.of("token",token));
    }
}
