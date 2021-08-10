package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpMethod;
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
        if (HttpMethod.GET.getHttpMethod().equals(method) && URI_WITHOUT_PARAMETERS.equals(path)) {
            content = tasksToJSON(taskMap);
            writeOutputStream(exchange, content, 200);
        }
        if (HttpMethod.GET.getHttpMethod().equals(method) && !URI_WITHOUT_PARAMETERS.equals(path)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                writeOutputStream(exchange, content, 404);
                return;
            }
            content = taskToJSON(task);
            writeOutputStream(exchange, content, 200);
        }
        if (HttpMethod.POST.getHttpMethod().equals(method)) {
            Task task = toTask(body);
            taskMap.put(task.getId(), task);

            Long lastSequence = Task.getSequence();
            Task lastTask = taskMap.get(lastSequence);

            content = taskToJSON(lastTask);
            writeOutputStream(exchange, content, 201);
        }
        if (HttpMethod.PUT.getHttpMethod().equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                writeOutputStream(exchange, content, 404);
                return;
            }

            Task changeTask = toTask(body);
            task.setTitle(changeTask.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);
            writeOutputStream(exchange, content, 200);
        }
        if (HttpMethod.PATCH.getHttpMethod().equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                writeOutputStream(exchange, content, 404);
                return;
            }
            Title title = toTitle(body);  //body를 직접 쓸시 인코딩 깨짐 문제 존재. 인코딩 방식 찾지 못해 Title 객체 생성해서 사용.
            task.setTitle(title.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);
            writeOutputStream(exchange, content, 200);
        }
        if (HttpMethod.DELETE.getHttpMethod().equals(method)) {
            Long id = getId(path);
            Task task = taskMap.get(id);
            if (task == null) {
                writeOutputStream(exchange, content, 404);
                return;
            }

            taskMap.remove(id);
            content = "";
            writeOutputStream(exchange, content, 204);
        }
    }
}
