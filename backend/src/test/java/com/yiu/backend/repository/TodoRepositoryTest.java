package com.yiu.backend.repository;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.Todo;
import com.yiu.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByTaskListId() {
        // 1. Arrange: User -> List -> Todos
        User user = new User();
        user.setUsername("task_owner");
        user.setPassword("pass");
        userRepository.save(user);

        TaskList list = new TaskList();
        list.setName("Shopping");
        list.setUser(user);
        taskListRepository.save(list);

        Todo t1 = new Todo();
        t1.setTitle("Milk");
        t1.setTaskList(list);
        todoRepository.save(t1);

        Todo t2 = new Todo();
        t2.setTitle("Eggs");
        t2.setTaskList(list);
        todoRepository.save(t2);

        // 2. Act
        List<Todo> tasks = todoRepository.findByTaskListId(list.getId());

        // 3. Assert
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Todo::getTitle).contains("Milk", "Eggs");
    }
}