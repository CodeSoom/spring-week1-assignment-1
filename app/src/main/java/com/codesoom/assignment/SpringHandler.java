package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpringHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();



        System.out.println(method + " " + path);

        if(path.startsWith("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if(path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            handleItem(exchange, method, id);
            return;
        }

        send(exchange, 200, "매일 매일 달리지기 위한 첫걸음 시작하기!");
    }




    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            send(exchange, 200, toJSON(tasks));
        }

        if(method.equals("POST")) {
//
            String body = getBody(exchange);

            Task task = toTask(body);
            task.setId(generateId());
            tasks.add(task);

            send(exchange, 201, toJSON(task));

        }
    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);


        if(task == null) {
            send(exchange, 404, "");
            return;
        }

        if(method.equals("GET")) {
            send(exchange, 200, toJSON(tasks));

        }

        if(method.equals("PUT") || method.equals("PATCH")) {

            String body = getBody(exchange);

            Task source = toTask(body);
            task.setTitle(source.getTitle());
            tasks.add(task);
            send(exchange, 200, toJSON(tasks));
        }

    }



    private Long generateId() {
        newId += 1;
        return newId;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }


    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }



    private Task toTask(String content) throws JsonProcessingException {
       return objectMapper.readValue(content, Task.class);

    }

    private String tasksToJSON() throws IOException {
        return toJSON(tasks);

    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }
}
