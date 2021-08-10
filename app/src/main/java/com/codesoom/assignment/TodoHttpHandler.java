package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.*;

public class TodoHttpHandler implements HttpHandler {

    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";
    private static final String URI_WITHOUT_PARAMETERS = "/tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String content = "";
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // TODO: 나중에 method들 enum으로 변경해보기
        if (GET.equals(method)) {
            if (URI_WITHOUT_PARAMETERS.equals(path)) {
                content = tasksToJSON(taskMap);
            } else {
                Long id = getId(path);
                Task task = taskMap.get(id);
                if (task == null) {
                    exchange.sendResponseHeaders(404, content.getBytes().length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
                content = taskToJSON(task);
            }
        } else if (POST.equals(method)) {
            if(!body.isBlank()) {
                Task task = toTask(body);
                taskMap.put(task.getId(), task);
                Long lastSequence = Task.getSequence();
                Task lastTask = taskMap.get(lastSequence);
                content = taskToJSON(lastTask);
                exchange.sendResponseHeaders(201, content.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                return;
            } else {
                content = "";
            }
        } else if (PUT.equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                exchange.sendResponseHeaders(404, content.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                return;
            }
            Task changeTask = toTask(body);
            task.setTitle(changeTask.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);

        } else if (PATCH.equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                exchange.sendResponseHeaders(404, content.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                return;
            }
            Title title = toTitle(body);  //body를 직접 쓸시 인코딩 깨짐 문제 존재. 인코딩 방식 찾지 못해 Title 객체 만들어서 body를 전환하는 걸로.
            task.setTitle(title.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);
        } else if (DELETE.equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                exchange.sendResponseHeaders(404, content.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(content.getBytes());
                outputStream.flush();
                outputStream.close();
                return;
            }
            taskMap.remove(id);
            content = "";
            exchange.sendResponseHeaders(204, content.getBytes().length);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
