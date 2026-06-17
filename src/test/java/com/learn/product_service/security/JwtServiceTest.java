package com.learn.product_service.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setup(){
        jwtService=new JwtService();
        ReflectionTestUtils.setField(jwtService,"secretKey","abishek-super-secret-key-must-be-32-chars-minimum-for-hs256");
        ReflectionTestUtils.setField(jwtService,"expiration",86400000L);
    }

    @Test
    void generateToken_returnsNotNullToken(){
        String token=jwtService.generateToken("abishek");
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_returnsCorrectName(){
        String token=jwtService.generateToken("abishek");
        String username=jwtService.extractUsername(token);
        assertThat(username).isEqualTo("abishek");
    }

    @Test
    void isTokenValid_validToken_returnsTrue(){
        String token=jwtService.generateToken("abishek");
        boolean valid=jwtService.isTokenValid(token,"abishek");
        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_InvalidToken_returnsFalse(){
        String token=jwtService.generateToken("abishek");
        boolean valid=jwtService.isTokenValid(token,"others");
        assertThat(valid).isFalse();
    }
}
