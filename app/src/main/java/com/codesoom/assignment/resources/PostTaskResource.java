package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class PostTaskResource extends TaskResource
    implements ResourceHandler {

    @Override
    public Response handleRequest(List<Task> tasks, String path, String body) throws JsonProcessingException {

        if (body.isBlank()) {
            return new Response("Body is empty, nothing to add", HttpStatusCode.OK);
        }
        Task task = super.toTask(body);
        task.generateId(tasks.size());
        tasks.add(task);

        return new Response(HttpStatusCode.CREATED.getStatus(), HttpStatusCode.CREATED);
    }
}
