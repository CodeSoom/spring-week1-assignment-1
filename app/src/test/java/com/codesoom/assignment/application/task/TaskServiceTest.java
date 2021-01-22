package com.codesoom.assignment.service;

import com.codesoom.assignment.application.task.TaskService;
import com.codesoom.assignment.domain.Task;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {
  TaskService taskService;

  @BeforeEach
  void setUp() {
    taskService = new TaskService();
  }

  @Test
  public void testGetTaskFail() {
    assertThrows(IllegalArgumentException.class, () -> taskService.getTask(0));
  }

  @Test
  public void testUpdateFail() {
    assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(0, "Study"));
  }

  @Test
  public void testDeleteFail() {
    assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(0));
  }

  @Test
  public void testGetEmptyTaskList() {
    List<Task> tasks = taskService.getTasks();
    assertNotNull(tasks);
    assertTrue(taskService.getTasks().isEmpty());
  }

  @Test
  public void testCreateTask() {
    String title = "Get Sleep";
    Task newTask = createTask(title);

    assertNotNull(newTask);
    assertEquals(title, newTask.getTitle());
  }

  @Test
  public void testUpdateTask() {
    String title = "Get Sleep";
    Task newTask = createTask(title);

    assertNotNull(newTask);
    assertEquals(title, newTask.getTitle());

    String newTitle = "Study";
    Task updatedTask = taskService.updateTask(newTask.getId(), newTitle);

    assertNotNull(updatedTask);
    assertEquals(newTitle, updatedTask.getTitle());
  }

  @Test
  public void testDeleteTask() {
    String title = "Get Sleep";
    Task newTask = createTask(title);

    assertNotNull(newTask);
    assertEquals(title, newTask.getTitle());
    assertEquals(1, taskService.getTasks().size());

    taskService.deleteTask(newTask.getId());
    assertTrue(taskService.getTasks().isEmpty());
  }

  Task createTask(String title) {
    return taskService.createNewTask(title);
  }
}
