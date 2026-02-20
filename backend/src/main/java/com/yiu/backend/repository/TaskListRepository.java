package com.yiu.backend.repository;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByUser(User user);
}
