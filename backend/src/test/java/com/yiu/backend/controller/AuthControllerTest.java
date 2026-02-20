package com.yiu.backend.controller;

import com.yiu.backend.model.User;
import com.yiu.backend.repository.UserRepository;
import com.yiu.backend.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void testSignup_Success() throws Exception {
        // 1. Setup: Username does not exist yet
        Mockito.when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        // 2. Perform POST and check for 201 Created
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"newuser\", \"password\": \"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    public void testSignup_Conflict() throws Exception {
        // 1. Setup: Username already exists
        User existingUser = new User();
        existingUser.setUsername("existing");
        Mockito.when(userRepository.findByUsername("existing")).thenReturn(Optional.of(existingUser));

        // 2. Perform POST and check for 409 Conflict
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"existing\", \"password\": \"password123\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        // 1. Setup: User exists in DB with encoded password
        String rawPassword = "password123";
        User dbUser = new User();
        dbUser.setUsername("loginuser");
        dbUser.setPassword(encoder.encode(rawPassword));

        Mockito.when(userRepository.findByUsername("loginuser")).thenReturn(Optional.of(dbUser));
        Mockito.when(jwtUtils.generateToken("loginuser")).thenReturn("mock-jwt-token");

        // 2. Perform POST and check for 200 OK and token
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"loginuser\", \"password\": \"" + rawPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void testLogin_Unauthorized() throws Exception {
        // 1. Setup: User exists but wrong password provided
        User dbUser = new User();
        dbUser.setUsername("user");
        dbUser.setPassword(encoder.encode("correct-password"));

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(dbUser));

        // 2. Perform POST with wrong password and check for 401
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user\", \"password\": \"wrong-password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}