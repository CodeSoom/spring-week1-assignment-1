package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
    private final List<Task> tasks = new ArrayList<>();
    int statusCode;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();


        InputStream inputStream = httpExchange.getRequestBody();


        System.out.println(method + " " + path);

        String content = "Hello, world!";

        Long pathId = Long.parseLong(getPathId(path));
        try {
            statusCode = 200;
            if (method.equals("GET") && path.equals("/tasks/" + pathId)) {
                content = taskToJson(pathId);
            }

            if (method.equals("GET") && path.equals("/tasks")) {
                content = tasksToJson();
            }

            if (method.equals("POST") && path.equals("/tasks")) {
                Task task = toTask(getBody(inputStream));
                tasks.add(task);
                content = task.toString();
                statusCode = 201;
            }

            if (method.equals("PUT") && path.equals("/tasks/" + pathId)) {
                content = modifyTaskById(getBody(inputStream), pathId);
            }

            if (method.equals("DELETE") && path.equals("/tasks/" + pathId)) {
                content = deleteTaskById(pathId);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            statusCode = 400;
            content = "옳바른 요청이 아닙니다.";
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            statusCode = 404;
            content = "요청에 해당하는 원소가 존재하지 않습니다.";
        }


        httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    // Get Request Body
    private String getBody(InputStream inputStream) throws IllegalArgumentException {
        String body = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        if (body.isEmpty()) {
            throw new IllegalArgumentException("옳바른 요청이 아닙니다.");
        }
        return body;
    }

    private String getPathId(String path) {
        String[] splitedPath = path.split("/");
        if (splitedPath.length > 2) {
            return splitedPath[splitedPath.length - 1];
        }
        return "0";
    }

    private Optional<Task> getOneTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    private String getTitleByBody(String content) {
        return content.split(": ")[1].split("}")[0];
    }

    private Task toTask(String content) {
        Long id = getLastId();
        // Value를 추출할때 split을 사용하여 value 추출
        String title = getTitleByBody(content);
        return new Task((id + 1L), title);
    }

    // Task List
    private String tasksToJson() {
        return tasks.toString();
    }

    // Find One Task By Id
    private String taskToJson(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> { throw new NoSuchElementException("요청에 해당하는 원소가 존재하지 않습니다."); }).toString();
    }

    // Modify One Task By Id
    private String modifyTaskById(String content, Long id) throws IllegalArgumentException, NoSuchElementException {
        Task findTask = getOneTaskById(id).orElseThrow(() -> { throw new NoSuchElementException("요청에 해당하는 원소가 존재하지 않습니다."); });
        int findTaskIdx = tasks.indexOf(findTask);
        findTask.setTitle(getTitleByBody(content));
        tasks.set(findTaskIdx, findTask);
        return findTask.toString();
    }

    // Delete Task By Id
    private String deleteTaskById(Long id) {
        Task findTask = getOneTaskById(id).orElseThrow(() -> { throw new NoSuchElementException("해당 아이디에 맞는 원소를 찾을수 없습니다."); });
        tasks.remove(findTask);
        return findTask.toString();
    }

    // 마지막 ID 추출
    private Long getLastId() {
        if (tasks.isEmpty()) {
            return 0L;
        }
        return tasks.get(tasks.size() - 1).getId();
    }

}
