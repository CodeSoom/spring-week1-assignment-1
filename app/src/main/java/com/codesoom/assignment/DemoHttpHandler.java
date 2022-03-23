package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private BodyContent bodyContent;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        bodyContent = new BodyContent(body);

        System.out.println(method + " " + path);

        String content = makeContent(method, path);

        int statusCode = 200;
        if(method.equals("POST")) {
            statusCode = 201;
        }

        httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getAlltasksToJSON() {
        String content = "[";
        for(Task task : tasks) {
            content += task.toString() + ",";
        }
        if(content.contains(",")) {
            content = content.substring(0, content.length() - 1);
        }

        return content + "]";
    }

    private String getLatestTaskToJSON()  {
        return tasks.get(tasks.size() - 1).toString();
    }

    private String getTaskToJSON(int index)  {
        if(index >= tasks.size()) {
            return "해당 id는 tasks에 없습니다.";
        }
        return tasks.get(index).toString();
    }

    private String makeContent(String method, String path) {
        String content = "";
        if(method.equals("GET")) {
            content = make_READ_content(path);
        }

        if(method.equals("DELETE")) {
            content = make_DELETE_content(path);
        }

        if(method.equals("POST")) {
            content = make_CREATE_content(path);
        }

        if(method.equals("PUT") || method.equals("PATCH")) {
            content = make_UPDATE_content(path);
        }

        return content;
    }

    private String make_UPDATE_content(String path) {
        String content = "";
        if(!bodyContent.getContent().equals("")
                && path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                Task task = new Task(number + 1L, bodyContent.getContent());
                if(number >= tasks.size()) {
                    return "해당 id는 tasks에 없습니다.";
                }
                tasks.set(number, task);

                content = getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        return content;
    }

    private String make_CREATE_content(String path) {
        String content = "";
        if(!bodyContent.getContent().equals("")
                && (path.equals("/tasks") || path.equals("/tasks/"))) {
            Task task = new Task(tasks.size() + 1L, bodyContent.getContent());
            tasks.add(task);

            content = getLatestTaskToJSON();
        }

        return content;
    }

    private String make_DELETE_content(String path) {
        String content = "";
        if(path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                if(number >= tasks.size()) {
                    return "해당 id는 tasks에 없습니다.";
                }
                tasks.remove(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        return content;
    }

    private String make_READ_content(String path) {
        String content = "";
        if(path.equals("/tasks") || path.equals("/tasks/")) {
            content = getAlltasksToJSON();
        }

        if(path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                content = getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        if(path.equals("/")) {
            content = "Hello World!";
        }

        return content;
    }
}