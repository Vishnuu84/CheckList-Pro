package com.yiu.backend.controller;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.User;
import com.yiu.backend.repository.TaskListRepository;
import com.yiu.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/lists")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskListController {

    @Autowired private TaskListRepository taskListRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping
    public List<TaskList> getLists(Principal principal) {
        // principal.getName() automatically contains the username from the JWT
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        return taskListRepository.findByUser(user);
    }

    @PostMapping
    public TaskList createList(@RequestBody TaskList taskList, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        taskList.setUser(user);
        return taskListRepository.save(taskList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteList(@PathVariable Long id, Principal principal) {
        // If the ID doesn't exist, this throws an exception handled by the code above
        TaskList list = taskListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("List ID " + id + " not found"));

        if (!list.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).body("You do not own this list!");
        }

        taskListRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}