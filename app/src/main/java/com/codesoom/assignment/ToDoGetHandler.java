package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.Optional;

public class ToDoGetHandler {

    private ToDoRepository repository;

    public ToDoGetHandler(ToDoRepository repository) {
        this.repository = repository;
    }

    public void handle(ToDoHttpResponder responder, String path) throws IOException {
        if ("/tasks".equals(path)) {
            sendGetResponseRoot(responder);
        } else if (path.matches("/tasks/\\d*")) {
            sendGetResponseWithId(responder, path);
        }
    }

    private void sendGetResponseWithId(ToDoHttpResponder responder, String path) throws IOException {
        Long taskId = -1L;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            responder.sendResponse(400, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            responder.sendResponse(200, repository.taskToString(task.get()));
        } else {
            responder.sendResponse(404, "Not found task by id");
        }
    }

    private void sendGetResponseRoot(ToDoHttpResponder responder) throws IOException {
        try {
            responder.sendResponse(200, repository.tasksToJSON());
        } catch (IOException e) {
            responder.sendResponse(500, "Failed to convert tasks to JSON");
        }
    }
}
