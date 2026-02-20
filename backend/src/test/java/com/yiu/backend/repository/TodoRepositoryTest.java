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
        // 1. Arrange: Setup User and a List with 2 tasks
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

    @Test
    public void testFindByTaskListId_Isolation() {
        // 1. Arrange: Create two separate lists for the same user
        User user = new User();
        user.setUsername("multi_list_user");
        user.setPassword("pass");
        userRepository.save(user);

        TaskList listA = new TaskList();
        listA.setName("Work");
        listA.setUser(user);
        taskListRepository.save(listA);

        TaskList listB = new TaskList();
        listB.setName("Personal");
        listB.setUser(user);
        taskListRepository.save(listB);

        // Add a task to List A
        Todo workTask = new Todo();
        workTask.setTitle("Finish Report");
        workTask.setTaskList(listA);
        todoRepository.save(workTask);

        // Add a task to List B
        Todo personalTask = new Todo();
        personalTask.setTitle("Buy Coffee");
        personalTask.setTaskList(listB);
        todoRepository.save(personalTask);

        // 2. Act: Search specifically for List A's ID
        List<Todo> results = todoRepository.findByTaskListId(listA.getId());

        // 3. Assert: Verify it ONLY returns List A's task
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Finish Report");
        assertThat(results.get(0).getTitle()).isNotEqualTo("Buy Coffee");
    }
}