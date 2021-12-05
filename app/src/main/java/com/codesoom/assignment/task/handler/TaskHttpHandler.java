package com.codesoom.assignment.task.handler;

import com.codesoom.assignment.response.ResponseBadRequest;
import com.codesoom.assignment.response.ResponseNotFound;
import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.handler.crud.DeleteTaskHandler;
import com.codesoom.assignment.task.handler.crud.DetailTaskHandler;
import com.codesoom.assignment.task.handler.crud.ListTaskHandler;
import com.codesoom.assignment.task.handler.crud.SaveTaskHandler;
import com.codesoom.assignment.task.handler.crud.UpdateTaskHandler;
import com.codesoom.assignment.task.service.TaskService;
import com.codesoom.assignment.task.validator.TaskValidator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class TaskHttpHandler implements HttpHandler {

    public static final String PATH = "/tasks";
    private static final String PATH_INCLUDE_SLASH = "/tasks/";

    private final TaskService taskService = new TaskService();
    private final TaskValidator taskValidator = new TaskValidator();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        if (path.equals(PATH)) {
            handleCollection(httpExchange, method);
            return;
        }

        if (path.startsWith(PATH_INCLUDE_SLASH)) {
            String stId = path.substring(PATH_INCLUDE_SLASH.length());

            if (!taskValidator.validTaskId(stId)) {
                new ResponseBadRequest(httpExchange).send("");
                return;
            }

            Long id = Long.parseLong(stId);
            handleItem(httpExchange, method, id);
            return;
        }
    }

    private void handleItem(HttpExchange httpExchange, String method, Long id) throws IOException {
        Task task = taskService.findByTaskId(id);

        if (task == null) {
            new ResponseNotFound(httpExchange).send("");
            return;
        }

        switch (method){
            case "GET":
                new DetailTaskHandler(taskService).doHandle(httpExchange, task);
                break;

            case "PUT":
            case "PATCH":
                new UpdateTaskHandler(taskService).doHandle(httpExchange, task);
                break;

            case "DELETE":
                new DeleteTaskHandler(taskService).doHandle(httpExchange, task);
                break;
        }
    }

    private void handleCollection(HttpExchange httpExchange, String method) throws IOException {
        switch (method) {
            case "GET":
                new ListTaskHandler(taskService).doHandle(httpExchange);
                break;

            case "POST":
                new SaveTaskHandler(taskService).doHandle(httpExchange);
                break;

            default:
                new ResponseNotFound(httpExchange).send("올바르지 않는 HTTP 메소드 입니다.");
                break;
        }
    }
}
