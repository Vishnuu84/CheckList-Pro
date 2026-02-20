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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @WithMockUser(username = "testuser")
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
        Todo mockTodo = new Todo();
        mockTodo.setTitle("Searchable Task");

        Mockito.when(todoRepository.findByTaskListId(1L))
                .thenReturn(Collections.singletonList(mockTodo));

        mockMvc.perform(get("/api/todos?taskListId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Searchable Task"));
    }

    // NEW: Test for Update (PUT)
    @Test
    @WithMockUser(username = "testuser")
    public void testUpdateTodo() throws Exception {
        Todo mockTodo = new Todo();
        mockTodo.setId(1L);
        mockTodo.setTitle("Updated Title");

        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(mockTodo));
        Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(mockTodo);

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"completed\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    // NEW: Test for Delete (DELETE)
    @Test
    @WithMockUser(username = "testuser")
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk());

        Mockito.verify(todoRepository, Mockito.times(1)).deleteById(1L);
    }
}