package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {

    TaskRepository taskRepository;
    IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();
        idGenerator = new IdGenerator();
    }

    @Test
    void addTask() {
        Long id = idGenerator.newId();
        Task task = new Task(id, "do something1!");

        taskRepository.save(task);

        assertEquals(task, taskRepository.findOne(id));

    }

    @Test
    void getEmptyTasks() {
        List<Task> tasks = taskRepository.findAll();

        assertEquals(0, tasks.size());
    }

    @Test
    void getTasks() {
        Task task1 = new Task(1L, "do something1!");
        Task task2 = new Task(2L, "do something2!");

        taskRepository.save(task1);
        taskRepository.save(task2);
        List<Task> tasks = taskRepository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void getTask() {
        Task task = new Task(1L, "do something1!");

        taskRepository.save(task);

        assertEquals(task, taskRepository.findOne(task.getId()));
    }

    @Test
    void updateTask() {
        Task task = new Task(1L, "do something1!");
        Task newTask = new Task(1L, "do nothing!");

        taskRepository.save(task);
        taskRepository.update(newTask.getId(), newTask);

        assertEquals(taskRepository.findOne(newTask.getId()), newTask);
    }

    @Test
    void deleteTask() {
        Task task = new Task(1L, "do something1!");

        taskRepository.save(task);
        taskRepository.delete(task.getId());

        assertNull(taskRepository.findOne(task.getId()));
    }

}
