package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DeleteTaskResource extends TaskResource
        implements ResourceHandler {
    @Override
    public Response handleRequest(List<Task> tasks, String path, String body) {

        if (path.startsWith(TASKS_ID)) {
            String param = path.substring(TASKS_ID.length());
            Long id = parseParam(param);
            Optional<Task> taskOpt = getTaskById(tasks, id);
            Task task = taskOpt.orElseThrow(() -> new NoSuchElementException("Not found"));
            return deleteTaskFromList(tasks, task);
        }
        return new Response(HttpStatusCode.NOT_FOUND.getStatus(), HttpStatusCode.NOT_FOUND);
    }

    private Response deleteTaskFromList(List<Task> tasks, Task task ) {
        if (tasks.remove(task)) {
            return new Response(HttpStatusCode.DELETED.getStatus(), HttpStatusCode.DELETED);
        }
        return new Response(HttpStatusCode.NOT_FOUND.getStatus(), HttpStatusCode.NOT_FOUND);
    }
}
