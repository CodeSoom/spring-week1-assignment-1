package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

        String pathId = getPathId(path).orElse("");
        try {
            statusCode = 200;
            if (method.equals(HttpMethod.GET.name()) && path.equals("/tasks/" + pathId)) {
                content = taskToJson(Long.valueOf(pathId));
            }

            if (method.equals(HttpMethod.GET.name()) && path.equals("/tasks")) {
                content = tasksToJson();
            }

            if (method.equals(HttpMethod.POST.name()) && path.equals("/tasks")) {
                Task task = toTask(getBody(inputStream));
                tasks.add(task);
                content = task.toString();
                statusCode = 201;
            }

            if (method.equals(HttpMethod.PUT.name()) && path.equals("/tasks/" + pathId)) {
                content = modifyTaskById(getBody(inputStream), Long.valueOf(pathId));
            }

            if (method.equals(HttpMethod.DELETE.name()) && path.equals("/tasks/" + pathId)) {
                content = deleteTaskById(Long.valueOf(pathId));
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            statusCode = 400;
            content = "올바른 요청이 아닙니다.";
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            statusCode = 404;
            content = "ID [" + pathId + "] 에 해당하는 Task가 존재하지 않습니다.";
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

    private Optional<String> getPathId(String path) {
        String[] splitedPaths = path.split("/");
        return Arrays.stream(splitedPaths)
                .filter(splitedPath -> splitedPath.matches("^-?\\d{1,19}$"))
                .findFirst();
    }

    private Optional<Task> getOneTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    private String getTitleByBody(String content) {
        String replacedContent = content
                .replace(" ", "")
                .replace("{", "")
                .replace("}", "");
        String[] splitedContents = replacedContent.split(":");
        // for문을 사용하여 출력하기
        for (int i = 0; i <= splitedContents.length; i++) {
            if (splitedContents[i].equals("\"title\"")) {
                return splitedContents[i + 1];
            }
        }
        return "";
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
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task.toString();
            }
        }

        throw new NoSuchElementException("ID [" + id + "]에 해당하는 Task를 찾을 수 없어, Task를 반환할 수 없습니다.");
    }

    // Modify One Task By Id
    private String modifyTaskById(String content, Long id) throws IllegalArgumentException, NoSuchElementException {
        Task findTask = getOneTaskById(id)
                .orElseThrow(() -> { throw new NoSuchElementException("ID [" + id + "]에 해당하는 Task를 찾을 수 없어, Task를 수정할 수 없습니다."); });
        int findTaskIdx = tasks.indexOf(findTask);
        findTask.setTitle(getTitleByBody(content));
        tasks.set(findTaskIdx, findTask);
        return findTask.toString();
    }

    // Delete Task By Id
    private String deleteTaskById(Long id) {
        Task findTask = getOneTaskById(id)
                .orElseThrow(() -> { throw new NoSuchElementException("ID [" + id + "]에 해당하는 Task를 찾을 수 없어, Task를 삭제할 수 없습니다."); });
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
