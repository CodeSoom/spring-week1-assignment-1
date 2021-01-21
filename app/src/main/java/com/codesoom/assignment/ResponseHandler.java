package com.codesoom.assignment;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.codesoom.assignment.JSONParser.*;
import static com.codesoom.assignment.TaskManager.*;

/**
 * Class handle responses content by requests
 *
 * @author Taeheon Woo
 * @version 1.0
 * TODO: if possible, change switch statement to hashmap
 *
 */
public class ResponseHandler {
    public String handle(String method, String path, List<Task> tasks, String body) throws IOException {
        // check wrong path
        if (!path.matches("/tasks/*[0-9]*")) return "Wrong URI path";

        Long taskId = extractTaskId(path);

        switch (method) {
            case "GET":
                return (path.equals("/tasks"))
                        ? tasksToJSON(tasks) // fetch task list
                        : taskToJSON(getTask(tasks, taskId)); // fetch a task

            case "POST":
                if (body.isBlank()) break;

                Task newTask = toTask(body, (long) (tasks.size() + 1));
                tasks.add(newTask);
                return taskToJSON(tasks.get(tasks.size() - 1));

            case "PUT": case "PATCH":
                if (body.isBlank()) break;

                Task editableTask = getTask(tasks, taskId);
                tasks.set(tasks.indexOf(editableTask), toTask(body, editableTask.getId()));
                return taskToJSON(getTask(tasks, taskId));

            case "DELETE":
                tasks.removeIf(task -> Objects.equals(taskId, task.getId()));
                return "";

            default:
                return "Unknown HTTP method";
        }

        return "Wrong URI path";
    }

    private Long extractTaskId(String path) {
        String[] splitPath = path.split("/");

        try {
            return Long.valueOf(splitPath[splitPath.length - 1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
