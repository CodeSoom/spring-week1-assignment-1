package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.ResponseHandlingException;
import com.codesoom.assignment.model.Task;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Handle responses content by requests.
 * TODO: if possible, change switch statement to hashmap.
 */
public class ResponseHandler {
    TaskHandler taskHandler = new TaskHandler();

    public String handle(String method, String path, List<Task> tasks, String body) throws IOException, ResponseHandlingException {
        if (path.equals("/") && HttpMethod.valueOf(method).equals(HttpMethod.HEAD)) {
            return "";
        }

        // check wrong path
        if (!path.matches("^/tasks(?:/[0-9]+)?$")) {
            throw new ResponseHandlingException(HttpStatusCode.NOT_FOUND);
        }

        Long taskId = extractTaskId(path);

        switch (HttpMethod.valueOf(method)) {
            case GET:
                // fetch task list
                if (path.equals("/tasks")) {
                    return taskHandler.handleFetch(tasks);
                }

                // fetch a task
                return taskHandler.handleFetch(tasks, taskId);

            case POST:
                if (body.isBlank()) { break; }
                return taskHandler.handleAdd(tasks, body);

            case PUT:
            case PATCH:
                if (body.isBlank()) { break; }
                return taskHandler.handleUpdate(tasks, taskId, body);

            case DELETE:
                return taskHandler.handleRemove(tasks, taskId);

            default:
                throw new ResponseHandlingException(HttpStatusCode.METHOD_NOT_ALLOWED);
        }

        throw new ResponseHandlingException(HttpStatusCode.NOT_FOUND);
    }

    @Nullable
    private Long extractTaskId(String path) {
        String[] splitPath = path.split("/");

        // return Longs.tryParse(splitPath[splitPath.length - 1])으로 대체 가능
        if (splitPath.length > 0 && splitPath[splitPath.length - 1].matches("^[0-9]+$")) {
            return Long.valueOf(splitPath[splitPath.length - 1]);
        }

        return null;
    }
}
