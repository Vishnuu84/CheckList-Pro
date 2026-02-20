package com.yiu.backend.controller;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.User;
import com.yiu.backend.repository.TaskListRepository;
import com.yiu.backend.repository.UserRepository;
import com.yiu.backend.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskListRepository taskListRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    public void testGetLists() throws Exception {
        // 1. Setup Mock User and Token
        String mockToken = "Bearer mock-token";
        String username = "testuser";
        User mockUser = new User();
        mockUser.setUsername(username);

        TaskList list = new TaskList();
        list.setId(1L);
        list.setName("My Work List");

        // 2. Define Mock Behavior
        Mockito.when(jwtUtils.extractUsername("mock-token")).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(taskListRepository.findByUser(mockUser)).thenReturn(Collections.singletonList(list));

        // 3. Perform GET and Assert
        mockMvc.perform(get("/api/lists")
                        .header("Authorization", mockToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("My Work List"));
    }
}