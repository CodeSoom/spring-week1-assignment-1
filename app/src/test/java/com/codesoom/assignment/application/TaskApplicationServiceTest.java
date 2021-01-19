package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.NotFoundTask;
import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskApplicationServiceTest {
    TaskApplicationService applicationService;

    @BeforeEach
    void initializeApplicationService() {
        applicationService = new TaskApplicationService();
    }

    @Test
    void getAllEmptyTasks() {
        List<Task> allTasks = applicationService.getAllTasks();
        assertNotNull(allTasks);
        assertEquals(0, allTasks.size());
    }

    @Test
    void getAllTasks() {
        String title = "Listen Music";
        applicationService.createTask(title);

        List<Task> allTasks = applicationService.getAllTasks();
        assertNotNull(allTasks);
        assertEquals(1, allTasks.size());
    }

    @Test
    void createTask() {
        String title = "Listen Music";
        Long taskId = applicationService.createTask(title);
        assertNotNull(taskId);
    }

    @Test
    void getSpecificTask() {
        String title = "Listen Music";
        Long taskId = applicationService.createTask(title);
        assertNotNull(taskId);
        assertDoesNotThrow(() -> {
            Task task = applicationService.findTask(taskId);
            assertNotNull(taskId);
            assertEquals(taskId, task.getId());
        });
    }

    @Test
    void getUncreatedTask() {
        Long uncreatedId = -1L;
        assertThrows(NotFoundTask.class, () -> applicationService.findTask(uncreatedId));
    }

    @Test
    void updateTask() {
        String title = "Listen Music";
        String newTitle = "Play Game!!";
        Long taskId = applicationService.createTask(title);
        assertNotNull(taskId);
        assertDoesNotThrow(() -> {
            applicationService.updateTaskTitle(taskId, newTitle);

            Task updatedTask = applicationService.findTask(taskId);
            assertEquals(taskId, updatedTask.getId());
            assertEquals(newTitle, updatedTask.getTitle());
        });
    }

    @Test
    void deleteTask() {
        String title = "Listen Music";
        Long taskId = applicationService.createTask(title);
        assertNotNull(taskId);

        assertDoesNotThrow(() -> applicationService.deleteTask(taskId));
        assertThrows(NotFoundTask.class, () -> applicationService.findTask(taskId));
    }

    @Test
    void deleteWrongTask() {
        Long uncreatedId = -1L;
        assertThrows(NotFoundTask.class, () -> applicationService.deleteTask(uncreatedId));
    }
    
    @Test
    void createTaskAfterDeleteTask() {
        String title = "Listen Music";
        Long taskId = applicationService.createTask(title);
        assertNotNull(taskId);

        assertDoesNotThrow(() -> {
            applicationService.deleteTask(taskId);
            Long newTaskId = applicationService.createTask(title);
            assertNotEquals(taskId, newTaskId);
        });
    }
}
