package com.etrichmond.driver_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationAndDriverFlowTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void registerLoginAndCreateDriverFlow() throws Exception {
        String registerJson = "{\"name\":\"System Admin\",\"email\":\"admin@example.com\",\"password\":\"StrongPass123!\",\"role\":\"SUPER_ADMIN\"}";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        String loginJson = "{\"email\":\"admin@example.com\",\"password\":\"StrongPass123!\"}";

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode loginPayload = objectMapper.readTree(loginResponse);
        String token = loginPayload.path("data").path("token").asText();

        String driverJson = "{\"firstName\":\"Ali\",\"lastName\":\"Bekele\",\"email\":\"driver@example.com\",\"phone\":\"+251911000000\",\"licenseNumber\":\"LIC-1001\",\"status\":\"ACTIVE\"}";

        mockMvc.perform(post("/api/drivers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(driverJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
