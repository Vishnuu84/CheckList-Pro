package com.yiu.backend.repository;

import com.yiu.backend.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Spring generates the SQL automatically from this name
    List<Todo> findByTaskListId(Long taskListId);
}