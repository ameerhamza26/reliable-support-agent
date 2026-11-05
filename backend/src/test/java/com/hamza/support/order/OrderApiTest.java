package com.hamza.support.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup
        .SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result
        .MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("local")
class OrderApiTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void customerCanReadOwnOrder() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1001")
                        .with(httpBasic("customer-a", "demo-a")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-1001"))
                .andExpect(jsonPath("$.status").value("SHIPPED"))
                .andExpect(jsonPath("$.estimatedDelivery")
                        .value("2026-11-07"))
                .andExpect(jsonPath("$.customerId").doesNotExist());
    }

    @Test
    void customerCannotReadAnotherCustomersOrder() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1002")
                        .with(httpBasic("customer-a", "demo-a")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.orderId").doesNotExist());
    }

    @Test
    void customerBCanReadOwnOrder() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1002")
                        .with(httpBasic("customer-b", "demo-b")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-1002"));
    }

    @Test
    void administratorCanReadAnotherCustomersOrder() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1002")
                        .with(httpBasic("support-admin", "demo-admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD-1002"));
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1001"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void incorrectPasswordIsRejected() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1001")
                        .with(httpBasic("customer-a", "wrong-password")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void nonexistentOrderReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/ORD-9999")
                        .with(httpBasic("customer-a", "demo-a")))
                .andExpect(status().isNotFound());
    }

    @Test
    void requestParametersCannotGrantAdministratorAccess()
            throws Exception {
        mockMvc.perform(get("/api/orders/ORD-1002")
                        .param("role", "admin")
                        .param("customerId", "customer-b")
                        .with(httpBasic("customer-a", "demo-a")))
                .andExpect(status().isNotFound());
    }
}