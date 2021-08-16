package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    public static final int POSITION_BY_ID_FROM_PATH = 2;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String PATH_DELIMITER = "/";
    private static final String TASK_PATH = "/tasks";
    private static final Pattern hasIdPattern = Pattern.compile("(/tasks)/(\\d+)/?$");
    private static final Pattern taskPatternWithoutId = Pattern.compile("(/tasks)/?$");

    private final TodoController todoController;
    private final ObjectMapper objectMapper;

    public DemoHttpHandler() {
        objectMapper = new ObjectMapper();
        todoController = new TodoController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestPath = exchange.getRequestURI().getPath();
        final String requestMethod = exchange.getRequestMethod();
        final String requestBody = getRequestBody(exchange);


        printRequestInfo(exchange);

        if (!isTaskPath(requestPath)) {
            sendResponse404(exchange);
            return;
        }

        if (isFindAllTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.findTasks());
            return;
        }

        if (isFindTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.findOne(extractIdFromRequestPath(requestPath)));
            return;
        }

        if (isSaveTask(requestMethod, requestPath)) {
            Task newTask = jsonToTask(requestBody);
            sendResponse(exchange, todoController.saveTask(newTask));
            return;
        }

        if (isUpdateTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.updateTask(extractIdFromRequestPath(requestPath), jsonToTask(requestBody)));
            return;
        }

        if (isDeleteTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.deleteTask(extractIdFromRequestPath(requestPath)));
            return;
        }

        sendResponse404(exchange);
    }

    private String getRequestBody(HttpExchange exchange) {
        return new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private void printRequestInfo(HttpExchange exchange) {
        final String logFormat = String.format("[%s] %s %s", LocalDateTime.now(),
                exchange.getRequestMethod(),
                exchange.getRequestURI());
        logger.info(logFormat);
    }

    private Task jsonToTask(String requestBody) throws IOException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private Long extractIdFromRequestPath(String requestPath) {
        final String taskId = requestPath.split(PATH_DELIMITER)[POSITION_BY_ID_FROM_PATH];

        return Long.valueOf(taskId);
    }

    private void sendResponse404(HttpExchange exchange) {
        sendResponse(exchange, Response.from(HttpURLConnection.HTTP_NOT_FOUND));
    }

    private void sendResponse(HttpExchange exchange, Response response) {
        try (final OutputStream os = exchange.getResponseBody()) {
            final byte[] responseByte = objectMapper.writeValueAsBytes(response.getBody());

            exchange.sendResponseHeaders(response.getStatusCode(), responseByte.length);
            os.write(responseByte);
            os.flush();

        } catch (IOException ie) {
            logger.severe((ie.getMessage()));
        }
    }

    private boolean isTaskPath(String requestPath) {
        return requestPath.startsWith(DemoHttpHandler.TASK_PATH);
    }

    private boolean isDeleteTask(String requestMethod, String requestPath) {
        return DELETE.equals(HttpMethod.from(requestMethod)) && isTaskPathWithId(requestPath);
    }

    private boolean isUpdateTask(String requestMethod, String requestPath) {
        return Arrays.asList(PUT, PATCH).contains(HttpMethod.from(requestMethod)) && isTaskPathWithId(requestPath);
    }

    private boolean isSaveTask(String requestMethod, String requestPath) {
        return POST.equals(HttpMethod.from(requestMethod)) && !isTaskPathWithId(requestPath);
    }

    private boolean isFindTask(String requestMethod, String requestPath) {
        return GET.equals(HttpMethod.from(requestMethod)) && isTaskPathWithId(requestPath);
    }

    private boolean isFindAllTask(String requestMethod, String requestPath) {
        return GET.equals(HttpMethod.from(requestMethod)) && isTaskPathWithoutId(requestPath);
    }

    private boolean isTaskPathWithId(String requestPath) {
        return matchesPattern(requestPath, hasIdPattern);
    }

    private boolean isTaskPathWithoutId(String requestPath) {
        return matchesPattern(requestPath, taskPatternWithoutId);
    }

    private boolean matchesPattern(String path, Pattern pattern) {
        return pattern.matcher(path).matches();
    }
}
