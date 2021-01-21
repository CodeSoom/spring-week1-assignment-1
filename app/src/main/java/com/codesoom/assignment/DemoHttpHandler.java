package com.codesoom.assignment;

import com.codesoom.assignment.errors.NotExistsIDException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * HTTP handler.
 *
 * <p>[POST]    /tasks       : 입력받은 태스크를 저장합니다.</p>
 * <p>[PUT]     /tasks/{id}  : 해당 id의 태스크를 수정합니다. (전체를 받아야 함)</p>
 * <p>[PATCH]   /tasks/{id}  : 해당 id의 태스크를 수정합니다. (부분만 받아도 반영해줌)</p>
 * <p>[DELETE]  /tasks/{id}  : 해당 id의 태스크를 삭제합니다.</p>
 */
public class DemoHttpHandler implements HttpHandler {
    private HttpExchange exchange;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Checks path starts with "/tasks".
     *
     * @param path is request uri.
     * @return true when path starts with "/tasks".
     */
    private boolean isValidPath(String path) {
        return Objects
                .requireNonNull(path)
                .startsWith("/tasks");
    }

    /**
     * "/tasks/{id}" parses long type id.
     *
     * @param path is request uri.
     * @return id.
     * @throws NumberFormatException when id is not number.
     * @throws NullPointerException  when id is null.
     */
    private long extractID(String path) throws NumberFormatException, NullPointerException {
        String[] split = path.split("/");

        if (split.length < 3) {
            throw new NullPointerException();
        }
        return Long.parseLong(split[2]);
    }

    /**
     * Send response code and content.
     *
     * @param content response content.
     * @param rCode   response code.
     * @throws IOException when can't response.
     */
    private void sendResponse(String content, int rCode) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(rCode, content.length());

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

        exchange.close();
    }

    /**
     * When path is "/tasks" or "/tasks/".
     *
     * @return all tasks.
     * @throws JsonProcessingException when json parse failed. (I don't think that's causing it.)
     */
    private String getAllTasks() throws JsonProcessingException {
        List<Task> tasks = TaskManager.find();
        return mapper.writeValueAsString(tasks);
    }

    /**
     * When path is "/tasks/{id}".
     *
     * @param path is request uri.
     * @return task.
     * @throws JsonProcessingException when json parse failed. (I don't think that's causing it.)
     * @throws NumberFormatException   when received id is not number format.
     * @throws NotExistsIDException    when task id is not exist.
     * @throws NullPointerException    when received id is null.
     */
    private String getTask(String path) throws JsonProcessingException, NumberFormatException, NotExistsIDException, NullPointerException {
        long id = extractID(path);
        Task task = TaskManager.find(id);
        return mapper.writeValueAsString(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.exchange = exchange;

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method + " " + uri.getPath());

        if (isValidPath(path)) {
            switch (method) {
                case "GET" -> {
                    if (path.equals("/tasks") || path.equals("/tasks/")) {
                        String content = getAllTasks();
                        sendResponse(content, 200);
                    } else {
                        try {
                            String content = getTask(path);
                            sendResponse(content, 200);
                        } catch (NotExistsIDException e) {
                            sendResponse("Not found task", 404);
                        } catch (NumberFormatException e) {
                            sendResponse("Inserted task id is not number", 400);
                        } catch (NullPointerException e) {
                            sendResponse("Not inserted id", 400);
                        }
                    }
                }
                case "POST" -> sendResponse("POST", 200);
                case "PUT" -> sendResponse("PUT", 200);
                case "PATCH" -> sendResponse("PATCH", 200);
                case "DELETE" -> sendResponse("DELETE", 200);
            }
        } else {
            sendResponse("Not found", 404);
        }
    }
}
