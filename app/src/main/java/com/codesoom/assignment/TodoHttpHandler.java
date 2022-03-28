package com.codesoom.assignment;

import com.codesoom.assignment.controller.TodoController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private TodoController todoController = new TodoController();

    public TodoHttpHandler() {

    }


    // String 타입의 문자를 Long 타입으로 변환하여 반환한다.
    public Long convertStringToLong(String str) {
        Long number;
        try {
            number = Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
        return number;
    }

    public String getRequestBody(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("/n"));
    }

    public HttpResponse handleHttpMethod(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        String path = uri.getPath();

        String[] splitedPath = path.split("/");
        String body = getRequestBody(exchange.getRequestBody());
        Long taskId = null;
        
        // "/tasks/{id}" 에서 id 추출하기
        if(splitedPath.length >= 3) {taskId = convertStringToLong(splitedPath[2]);}

        switch (method) {
            case "GET":
                if(path.equals("/tasks") || path.equals("/tasks/")) {
                    return todoController.findAllTasks();
                } else if(path.startsWith("/tasks/") && taskId != null) {
                    return todoController.findTaskById(convertStringToLong(splitedPath[2]));
                }
            case "POST":
                if(path.equals("/tasks") || path.equals("/tasks/")) {
                    return todoController.createTask(body);
                }
            case "PUT":
            case "PATCH":
                if(path.startsWith("/tasks/") && taskId != null) {
                    return todoController.updateTask(convertStringToLong(splitedPath[2]), body);
                }
            case "DELETE":
                if(path.startsWith("/tasks/") && taskId != null) {
                    return todoController.deleteTaskById(convertStringToLong(splitedPath[2]));
                }
            default:
                return new HttpResponse(404, "잘못된 요청입니다!");
        }

    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. GET /tasks
        // 2. GET /tasks/{id}
        // 3. POST /tasks
        // 4. PUT/PATCH /tasks/{id}
        // 5. DELETE /tasks/{id}

        HttpResponse httpResponse = handleHttpMethod(exchange);

        exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getResponse().getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(httpResponse.getResponse().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
