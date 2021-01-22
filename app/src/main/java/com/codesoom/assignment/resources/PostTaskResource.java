package com.codesoom.assignment.resources;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PostTaskResource extends TaskResource
    implements ResourceHandler {

    @Override
    public String handleRequest(String path, String body) throws JsonProcessingException {

        if (body.isBlank()) {
            return "Body is empty, nothing to add";
        }
        Task task = super.toTask(body);
        task.generateId(tasks.size());
        tasks.add(task);

        return "Create a new task!";
    }
}
