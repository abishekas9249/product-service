package com.learn.product_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.product_service.dto.ProductRequest;
import com.learn.product_service.model.Product;
import com.learn.product_service.security.JwtService;
import com.learn.product_service.security.SecurityConfig;
import com.learn.product_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private User user;

    @MockitoBean
    private UserDetails userDetails;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_withValidToken_returns200() throws Exception{
        List<Product> products = List.of(
                new Product(1L,"Laptop",75000.0,"Electronics"));
        when(productService.getAllProducts()).thenReturn(products);
        mockJwtAndUserDetails();
        mockMvc.perform(get("/api/products")
                .header("Authorization","Bearer faketoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                .value("Laptop"));

    }
    @Test
    void getAll_withoutToken_returns403() throws Exception{
        mockMvc.perform(get("/api/products")).andExpect(status().isForbidden());
    }
    @Test
    void create_validRequest_returns201() throws Exception{
        ProductRequest req = new ProductRequest(
                "Monitor", 18000.0, "Electronics");
        Product created = new Product(
                3L, "Monitor", 18000.0, "Electronics");

        when(productService.createProduct(any())).thenReturn(created);
        mockJwtAndUserDetails();
        mockMvc.perform(post("/api/products")
                .header("Authorization","Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Monitor"));
    }

    @Test
    void create_invalidRequest_returns400() throws Exception{
        ProductRequest bad = new ProductRequest(
                "", -1.0, "");
        mockJwtAndUserDetails();
        mockMvc.perform(post("/api/products")
                .header("Authorization","Bearer faketoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400));

    }

    @Test
    void delete_validRequest_returns200() throws Exception{
        doNothing().when(productService).delete(1L);
        mockJwtAndUserDetails();
        mockMvc.perform(delete("/api/products/{id}",1)
                .header("Authorization","Bearer fakeToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product1 has been successfully deleted."));
    }
    private void mockJwtAndUserDetails() {
        when(jwtService.extractUsername(any())).thenReturn("abishek");
        when(jwtService.isTokenValid(any(),any())).thenReturn(true);
        UserDetails userDetails=User.builder()
                .username("abishek")
                .password("abis123")
                .roles("ADMIN")
                .build();
        when(userDetailsService.loadUserByUsername("abishek")).thenReturn(userDetails);
    }
}
