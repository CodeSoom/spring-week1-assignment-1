package com.codesoom.assignment.handler;

import com.codesoom.assignment.controllers.ExceptionController;
import com.codesoom.assignment.controllers.IdController;
import com.codesoom.assignment.controllers.TaskController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TaskHandler implements HttpHandler {

    public static final String HANDLER_PATH = "/tasks";

    private static final int INDEX_START = 0;
    private static final int ID_INDEX = 1;

    private static final String EMPTY_STRING = "";
    private static final String PATH_DELIMITER = "/";

    private final TaskController taskController;
    private final IdController idController;
    private final ExceptionController exceptionController;

    public TaskHandler() {
        taskController = new TaskController();
        idController = new IdController();
        exceptionController = new ExceptionController();
    }

    private Optional<Long> parseId(final String idString) {
        Long taskId = null;
        try {
            taskId = Long.parseLong(idString);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(taskId);
    }

    private void handleId(
            final HttpExchange exchange, final String method, final String path
    ) throws IOException {

        if (path.charAt(INDEX_START) != PATH_DELIMITER.charAt(INDEX_START)) {
            exceptionController.handleInvalidRequest(exchange);
            return;
        }

        final String[] paths = path.split(PATH_DELIMITER);
        if (ID_INDEX + 1 != paths.length) {
            exceptionController.handleInvalidRequest(exchange);
            return;
        }

        final Optional<Long> taskIdOptional = parseId(paths[ID_INDEX]);
        if (taskIdOptional.isEmpty()) {
            exceptionController.handleInvalidId(exchange);
            return;
        }

        if (HttpMethod.GET.name().equals(method)) {
            idController.handleGet(exchange, taskIdOptional.get());
            return;
        }

        if (HttpMethod.PATCH.name().equals(method)) {
            idController.handlePatch(exchange);
            return;
        }

        if (HttpMethod.DELETE.name().equals(method)) {
            idController.handleDelete(exchange);
            return;
        }

        final String[] allowedMethods = new String[] {HttpMethod.GET.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name()};
        exceptionController.handleInvalidMethod(exchange, path, allowedMethods);
    }


    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath().replace(HANDLER_PATH, EMPTY_STRING);

        final String requestBody = new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        if (!EMPTY_STRING.equals(path)) {
            handleId(exchange, method, path);
            return;
        }

        if (HttpMethod.GET.name().equals(method)) {
            taskController.handleGet(exchange);
            return;
        }

        if (HttpMethod.POST.name().equals(method)) {
            taskController.handlePost(exchange, requestBody);
            return;
        }

        final String[] allowedMethods = new String[] {HttpMethod.GET.name(), HttpMethod.POST.name()};
        exceptionController.handleInvalidMethod(exchange, path, allowedMethods);
    }
}
