package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String PATH_DELIMITER = "/";
    private static final String TASK_PATH = "/tasks";
    private static final Pattern hasIdPattern = Pattern.compile("(/tasks)/(\\d+)/?$");

    private final TodoController todoController;
    private final ObjectMapper om;

    public DemoHttpHandler() {
        om = new ObjectMapper();
        todoController = new TodoController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestPath = exchange.getRequestURI().getPath();
        final String requestMethod = exchange.getRequestMethod();

        printRequestInfo(exchange);

        if (!isExactPath(requestPath)) {
            sendResponse404(exchange);
            return;
        }

        if (isFindAllTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.findTasks());
            return;
        }

        if (isFindTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.findOne(extractId(requestPath)));
            return;
        }

        if (isSaveTask(requestMethod, requestPath)) {
            Task newTask = jsonToTask(exchange);
            sendResponse(exchange, todoController.saveTask(newTask));
            return;
        }

        if (isUpdateTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.updateTask(extractId(requestPath), jsonToTask(exchange)));
            return;
        }

        if (isDeleteTask(requestMethod, requestPath)) {
            sendResponse(exchange, todoController.deleteTask(extractId(requestPath)));
        }
    }

    private void printRequestInfo(HttpExchange exchange) {
        final String logFormat = String.format("[%s] %s %s", LocalDateTime.now(),
                exchange.getRequestMethod(),
                exchange.getRequestURI());
        logger.info(logFormat);
    }

    private Task jsonToTask(HttpExchange exchange) throws IOException {
        final InputStream body = exchange.getRequestBody();
        return om.readValue(body, Task.class);
    }

    private Long extractId(String requestPath) {
        return Long.valueOf(requestPath.split(PATH_DELIMITER)[2]);
    }

    private void sendResponse(HttpExchange exchange, Response response) {
        try (final OutputStream os = exchange.getResponseBody()) {
            final byte[] responseByte = om.writeValueAsBytes(response.getBody());

            exchange.sendResponseHeaders(response.getStatusCode(), responseByte.length);
            os.write(responseByte);
            os.flush();

        } catch (IOException ie) {
            logger.severe((ie.getMessage()));
        }
    }

    private boolean hasIdBetween(String requestPath) {
        final Matcher matcher = hasIdPattern.matcher(requestPath);
        return matcher.matches();
    }

    private void sendResponse404(HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0L);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }

    }

    private boolean isExactPath(String requestPath) {
        return requestPath.startsWith(DemoHttpHandler.TASK_PATH);
    }

    private boolean isDeleteTask(String requestMethod, String requestPath) {
        return DELETE.equals(HttpMethod.from(requestMethod)) && hasIdBetween(requestPath);
    }

    private boolean isUpdateTask(String requestMethod, String requestPath) {
        return Arrays.asList(PUT, PATCH).contains(HttpMethod.from(requestMethod)) && hasIdBetween(requestPath);
    }

    private boolean isSaveTask(String requestMethod, String requestPath) {
        return POST.equals(HttpMethod.from(requestMethod)) && !hasIdBetween(requestPath);
    }

    private boolean isFindTask(String requestMethod, String requestPath) {
        return GET.equals(HttpMethod.from(requestMethod)) && hasIdBetween(requestPath);
    }

    private boolean isFindAllTask(String requestMethod, String requestPath) {
        return GET.equals(HttpMethod.from(requestMethod)) && !hasIdBetween(requestPath);
    }
}
