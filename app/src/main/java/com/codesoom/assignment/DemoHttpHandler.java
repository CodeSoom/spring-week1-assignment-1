package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

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

        System.out.println(method + " " + path);

        String content = "Hello, World!";

        int statusCode = 200;

        if(method.equals("GET") && (path.equals("/tasks") || path.equals("/tasks/"))) {
            content = getAlltasksToJSON();
        }

        if(method.equals("GET") && path.contains("/tasks/")
                && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                content = getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        if(method.equals("DELETE") && path.contains("/tasks/")
                && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                tasks.remove(number);
                content = "";
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        if(!body.isEmpty() && method.equals("POST")
                && (path.equals("/tasks") || path.equals("/tasks/"))) {
            Task task = toTask(body);
            task.setId(tasks.size() + 1L);
            tasks.add(task);

            content = getLatestTaskToJSON();

            statusCode = 201;
        }

        if(!body.isEmpty() && method.equals("PUT") && path.contains("/tasks/")
        && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                Task task = toTask(body);
                task.setId(number + 1L);

                tasks.set(number, task);

                content = getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) {
        // 유니코드를 한글로 변환할 버퍼 선언
        StringBuffer sb = new StringBuffer();
        // 글자를 하나하나 탐색하면서 유니코드만 버퍼에 담는다.
        for (int i = 0; i < content.length(); i++) {
            // 조합이 u로 시작하면 6글자를 변환한다.
            if ('\\' == content.charAt(i) && 'u' == content.charAt(i + 1)) {
                // 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
                Character r = (char) Integer.parseInt(content.substring(i + 2, i + 6), 16);
                // 유니코드에서 한글로 변환된 글자를 버퍼에 넣는다.
                sb.append(r);
                // for의 증가 값 1과 5를 합해 6글자를 점프
                i += 5;
            }
        }
        // 결과 리턴
        return new Task(null, sb.toString());
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
        return tasks.get(index).toString();
    }
}