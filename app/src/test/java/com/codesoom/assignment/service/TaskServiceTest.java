package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    final TaskService taskService = new TaskService();

    @Test
    @DisplayName("Task를 생성하는 요청이 들어왔을 때, createTask 메서드가 Task를 생성하고 생성한 테스트를 리턴하는 것을 테스트한다.")
    void createTaskTest() {
        Task taskToEquals = new Task(0L, "BJP");
        assertEquals(taskToEquals, taskService.createTask("BJP"));
    }

    @Test
    @DisplayName("전체 Tasks를 조회하는 요청이 들어왔을 때, getTasks 메서드가 저장되어 있는 Tasks를 리턴하는 것을 테스트한다.")
    void getTasksTest() {
        assertEquals(0, taskService.getTasks().size());
    }

    @Test
    @DisplayName("Task를 조회하는 요청이 들어왔고 숫자 형식의 id와 같은 Task가 존재할 때, getTask 메서드가 해당 Task를 리턴한다.")
    void getTaskTest() {
        taskService.createTask("BJP");
        assertEquals(new Task(0L, "BJP"), taskService.getTask(0L));
    }
}