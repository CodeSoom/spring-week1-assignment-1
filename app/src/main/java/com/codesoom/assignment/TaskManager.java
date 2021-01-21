package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * Class have methods that return Task object by arguments
 *
 * @author Taeheon Woo
 * @version 1.0
 *
 */
public class TaskManager {
    static Task toTask(String content, Long id) throws JsonProcessingException {
        Task task = new ObjectMapper().readValue(content, Task.class);
        task.setId(id);
        return task;
    }

    static Task getTask(List<Task> tasks, Long taskId) {
        return tasks.stream()
                .filter(element -> Objects.equals(taskId, element.getId()))
                .findFirst()
                .orElse(new Task());
    }
}
