package com.yiu.backend.repository;

import com.yiu.backend.model.TaskList;
import com.yiu.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskListRepositoryTest {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUser() {
        // 1. Arrange: Create a user and two lists for them
        User user = new User();
        user.setUsername("relation_tester");
        user.setPassword("pass");
        userRepository.save(user);

        TaskList list1 = new TaskList();
        list1.setName("Work");
        list1.setUser(user);
        taskListRepository.save(list1);

        TaskList list2 = new TaskList();
        list2.setName("Personal");
        list2.setUser(user);
        taskListRepository.save(list2);

        // 2. Act: Fetch lists by user
        List<TaskList> results = taskListRepository.findByUser(user);

        // 3. Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(TaskList::getName).containsExactlyInAnyOrder("Work", "Personal");
    }
}