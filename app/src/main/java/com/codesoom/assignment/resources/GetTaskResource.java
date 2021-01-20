package com.codesoom.assignment.resources;

import com.codesoom.assignment.Constants;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.InvalidRequestStateException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class GetTaskResource extends TaskResource
        implements ResourceHandler {

    @Override
    public String handleRequest(String path, String body) throws IOException {

        System.out.println("Get Task Resource path = " + path);
        if (path.equals(Constants.TASKS)) {
            return tasksToJSON();
        }
        if (path.startsWith(Constants.TASKS_ID)) {
            String param = path.substring(Constants.TASKS_ID.length());
            Long id = parseParam(param);
            Optional<Task> taskOpt = getTaskById(id);
            Task task = taskOpt.orElseThrow(() -> new NoSuchElementException("Not found"));
            return taskToJSON(task);
        }
        return null;
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
