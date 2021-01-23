package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.URLs;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.sun.jdi.request.InvalidRequestStateException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class GetTaskResource extends TaskResource
        implements ResourceHandler {

    @Override
    public Response handleRequest(String path, String body) throws IOException {

        if (path.equals(URLs.TASKS)) {
            return new Response(tasksToJSON(), HttpStatusCode.OK);
        }
        if (path.startsWith(URLs.TASKS_ID)) {
            String param = path.substring(URLs.TASKS_ID.length());
            Long id = parseParam(param);
            Optional<Task> taskOpt = getTaskById(id);
            Task task = taskOpt.orElseThrow(() -> new NoSuchElementException("Not found"));
            return new Response(taskToJSON(task), HttpStatusCode.OK);
        }
        return new Response(HttpStatusCode.BAD_REQUEST.getStatus(), HttpStatusCode.BAD_REQUEST);
    }

    private Long parseParam(String param) {
        if (param.isBlank()) {
            throw new InvalidRequestStateException("No Id passed");
        }
        return Long.parseLong(param);
    }

    private Optional<Task> getTaskById(Long id) {
        return tasks.stream()
                .filter(i -> i.getId() == id)
                .reduce((i, v) -> {
                    throw new IllegalStateException("More than one ID exist");
                });
    }
}
