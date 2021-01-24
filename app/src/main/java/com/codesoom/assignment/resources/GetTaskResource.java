package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GetTaskResource extends TaskResource
        implements ResourceHandler {

    @Override
    public Response handleRequest(List<Task> tasks, String path, String body) throws IOException {

        if (path.equals("/")) {
            return new Response(HttpStatusCode.OK.getStatus(), HttpStatusCode.OK);
        }

        if (path.equals(TASKS)) {
            return new Response(tasksToJSON(tasks), HttpStatusCode.OK);
        }
        if (path.startsWith(TASKS_ID)) {
            String param = path.substring(TASKS_ID.length());
            Long id = parseParam(param);
            return findTask(tasks, id);
        }
        return new Response(HttpStatusCode.BAD_REQUEST.getStatus(), HttpStatusCode.BAD_REQUEST);
    }

    public Response findTask(List<Task> tasks, Long id) throws IOException {
        if (id == null) {
            return new Response(HttpStatusCode.NOT_FOUND.getStatus(), HttpStatusCode.NOT_FOUND);
        }
        Optional<Task> taskOpt = getTaskById(tasks, id);
        Task task = taskOpt.orElse(null);
        if (task != null) {
            return new Response(taskToJSON(task), HttpStatusCode.OK);
        }
        return new Response(HttpStatusCode.NOT_FOUND.getStatus(), HttpStatusCode.NOT_FOUND);
    }
}
