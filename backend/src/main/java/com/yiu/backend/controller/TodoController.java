package com.yiu.backend.controller;

import com.yiu.backend.model.Todo;
import com.yiu.backend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {

    @Autowired private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getTodos(@RequestParam Long taskListId) {
        // We filter by List ID, and security is handled at the filter level
        return todoRepository.findByTaskListId(taskListId);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        return todoRepository.save(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}