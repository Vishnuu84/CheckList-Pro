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
import org.springframework.security.test.context.support.WithMockUser; // Required for Principal
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @WithMockUser(username = "testuser") // Mocks the Principal user
    public void testCreateTodo() throws Exception {
        TaskList mockList = new TaskList();
        mockList.setId(1L);

        Todo mockTodo = new Todo();
        mockTodo.setTitle("Unit Test Task");
        mockTodo.setTaskList(mockList);

        Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(mockTodo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Unit Test Task\", \"taskList\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Unit Test Task"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetTodosByListId() throws Exception {
        // This test ensures the search bar's data source is verified
        Todo mockTodo = new Todo();
        mockTodo.setTitle("Searchable Task");

        Mockito.when(todoRepository.findByTaskListId(1L))
                .thenReturn(Collections.singletonList(mockTodo));

        mockMvc.perform(get("/api/todos?taskListId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Searchable Task"));
    }
}