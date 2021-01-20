package com.codesoom.assignment;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.codesoom.assignment.JSONParser.*;

public class ResponseHandler {
    Task getTask(List<Task> tasks, Long taskId) {
        Optional<Task> optionalTask = tasks.stream()
                .filter(element -> taskId == element.getId())
                .findFirst();

        try {
            return optionalTask.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return new Task();
    }

    public String handle(String method, String path, List<Task> tasks, String body) throws IOException {
        String content = "";

        // check wrong path
        if (!path.matches("/tasks/*[0-9]*")) {
            return "Wrong URI path";
        }

        Long taskId = extractTaskId(path);

        switch (method) {
            case "GET":
                // fetch task list
                if (path.equals("/tasks")) {
                    return tasksToJSON(tasks);
                }

                // fetch a task
                Optional<Task> optionalTask = tasks.stream()
                        .filter(element -> taskId == element.getId())
                        .findFirst();

                try {
                    return taskToJSON(optionalTask.get());
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }

                break;

            case "POST":
                if (!body.isBlank()) {
                    Task task = toTask(body, Long.valueOf(tasks.size() + 1));
                    tasks.add(task);
                }

                return taskToJSON(tasks.get(tasks.size() - 1));

            case "PUT": case "PATCH":
                if (!body.isBlank()) {
                    Task task = getTask(tasks, taskId);
                    int index = tasks.indexOf(task);
                    tasks.set(index, toTask(body, task.getId()));

                    return handle("GET", path, tasks, body);
                }

                break;

            case "DELETE":
                if (taskId != null) {
                    tasks.removeIf(task -> taskId == task.getId());
                    return "";
                }

                break;

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
