package com.codesoom.assignment.impl;

import com.codesoom.assignment.IAssignmentService;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignmentService implements IAssignmentService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getAll() {
        return tasks;
    }

    @Override
    public Optional<Task> create(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        if (isTitlePresent(task.getTitle())) {
            return Optional.empty();
        }
        task.setId((long) (tasks.size() + 1));
        tasks.add(task);
        return Optional.of(task);
    }

    private Boolean isTitlePresent(String title) {
        return title.isEmpty();
    }
}
