package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.ResponseHandlingException;
import com.codesoom.assignment.model.Task;
import com.google.common.primitives.Longs;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.codesoom.assignment.helper.JSONParser.*;
import static com.codesoom.assignment.helper.TaskManager.*;

/**
 * Handle responses content by requests.
 * TODO: if possible, change switch statement to hashmap.
 */
public class ResponseHandler {
    public String handle(String method, String path, List<Task> tasks, String body) throws IOException, ResponseHandlingException {
        // check wrong path
        if (!path.matches("^/tasks(/[0-9]+$)?")) {
            throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.NOT_FOUND);
        }

        Long taskId = extractTaskId(path);

        switch (method) {
            case HttpMethod.GET:
                // fetch task list
                if (path.equals("/tasks")) {
                    return tasksToJSON(tasks);
                }

                // fetch a task
                Task selectedTask = getTask(tasks, taskId);
                // TODO: getTask에서 아래의 로직을 처리할 수 있도록 함
                if (selectedTask.getId() == null) {
                    throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.NOT_FOUND);
                }

                return taskToJSON(selectedTask);

            case HttpMethod.POST:
                if (body.isBlank()) { break; }

                Task newTask = toTask(body, (long) (tasks.size() + 1));
                tasks.add(newTask);
                return taskToJSON(tasks.get(tasks.size() - 1));

            case HttpMethod.PUT:
            case HttpMethod.PATCH:
                if (body.isBlank()) { break; }

                Task editableTask = getTask(tasks, taskId);
                // TODO: getTask에서 아래의 로직을 처리할 수 있도록 함
                if (editableTask.getId() == null) {
                    throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.NOT_FOUND);
                }

                tasks.set(tasks.indexOf(editableTask), toTask(body, editableTask.getId()));
                return taskToJSON(getTask(tasks, taskId));

            case HttpMethod.DELETE:
                if (getTask(tasks, taskId).getId() == null) {
                    throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.NOT_FOUND);
                }

                tasks.removeIf(task -> Objects.equals(taskId, task.getId()));
                return "";

            default:
                throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.METHOD_NOT_ALLOWED);
        }

        throw new ResponseHandlingException(ResponseHandlingException.ErrorCode.NOT_FOUND);
    }

    @Nullable
    private Long extractTaskId(String path) {
        String[] splitPath = path.split("/");

        // return Longs.tryParse(splitPath[splitPath.length - 1])으로 대체 가능
        return splitPath[splitPath.length - 1].matches("^[0-9]+$")
                ? Long.valueOf(splitPath[splitPath.length - 1])
                : null;
    }
}
