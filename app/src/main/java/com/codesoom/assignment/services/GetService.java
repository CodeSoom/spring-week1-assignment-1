package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.JsonConverter;
import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Collection;

public class GetService {

    private static final GetService instance = new GetService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private GetService() {
    }

    public static GetService getInstance() {
        return instance;
    }

    public String service(Long id, HttpExchange exchange) throws IOException {
        String content;

        if (id == null) {
            Collection<Task> allTasks = taskRepository.getAllTasks();
            content = JsonConverter.tasksToJson(allTasks);
            exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);
            return content;
        }

        Task task = taskRepository.findById(id);
        if (task == null) {
            content = "";
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, content.getBytes().length);
            return content;
        }

        content = JsonConverter.taskToJson(task);
        exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);
        return content;
    }
}
