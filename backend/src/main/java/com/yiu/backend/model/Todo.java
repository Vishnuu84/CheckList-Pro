package com.yiu.backend.model;

import jakarta.persistence.*;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }
}
