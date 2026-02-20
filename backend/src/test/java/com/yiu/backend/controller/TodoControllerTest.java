package com.yiu.backend.controller;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.Todo;
import com.yiu.backend.repository.TaskListRepository;
import com.yiu.backend.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoRepository todoRepository;

    @MockitoBean
    private TaskListRepository taskListRepository;

    @Test
    public void testCreateTodo() throws Exception {
        // 1. Setup Mock Data
        TaskList mockList = new TaskList();
        mockList.setId(1L);
        mockList.setName("Test List");

        Todo mockTodo = new Todo();
        mockTodo.setTitle("Unit Test Task");
        mockTodo.setTaskList(mockList);

        // 2. Define Mock Behavior
        Mockito.when(taskListRepository.findById(1L)).thenReturn(Optional.of(mockList));
        Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(mockTodo);

        // 3. Perform Request and Assert
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Unit Test Task\", \"taskList\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Unit Test Task"));
    }
}