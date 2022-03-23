package com.codesoom.assignment.handlers;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks;

    private static final String PATH = "/tasks";

    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_CREATE = 201;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public TaskHttpHandler() {
        this.tasks = new ArrayList<>();
    }

    private boolean isMatch(String requestURI) {
        final String regex = "^\\/tasks\\/[0-9]+$";
        final Pattern pattern = Pattern.compile(regex);

        final Matcher matcher = pattern.matcher(requestURI);
        return matcher.matches();
    }

    private Long getPathVariable(String requestURI) {
        String[] splitPaths = requestURI.split("/");
        return Long.valueOf(splitPaths[2]);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().getPath();

        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod + " " + exchange.getRequestURI());

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "application/json");

        OutputStream outputStream = exchange.getResponseBody();
        String responseBody = "";
        int httpStatus = 0;

        if (PATH.equals(requestURI)) {
            if (HttpMethod.GET.name().equals(requestMethod)) {
                System.out.println(" - ToDo 전체 조회 요청");

                responseBody = tasksToJSON();
                httpStatus = HTTP_STATUS_OK;
            }

            if (HttpMethod.POST.name().equals(requestMethod)) {
                System.out.println(" - ToDo 추가 요청");

                final String requestBody = getRequestBody(exchange.getRequestBody());
                if (requestBody.isBlank()) {
                    System.out.println(" - ToDo 추가 ERROR: 요청 값이 없음");
                    responseBody = "할 일을 입력해 주세요.";
                    httpStatus = HTTP_STATUS_BAD_REQUEST;

                } else {
                    try {
                        TaskDto taskDto = toTask(requestBody);
                        responseBody = taskToJSON(addTask(taskDto));
                        httpStatus = HTTP_STATUS_CREATE;
                    } catch (IllegalArgumentException e) {
                        responseBody = e.getMessage();
                        httpStatus = HTTP_STATUS_BAD_REQUEST;
                    }
                }
            }
        } else if (isMatch(requestURI)) {
            Long taskId = getPathVariable(requestURI);
            if (HttpMethod.GET.name().equals(requestMethod)) {
                Task findTask = findTaskById(taskId);
                responseBody = taskToJSON(findTask);
                httpStatus = HTTP_STATUS_OK;
            }
            if (HttpMethod.PATCH.name().equals(requestMethod)) {
                final String requestBody = getRequestBody(exchange.getRequestBody());
                responseBody = taskToJSON(updateTaskById(taskId, toTask(requestBody).getTitle()));
                httpStatus = HTTP_STATUS_OK;
            }
            if (HttpMethod.DELETE.name().equals(requestMethod)) {
                deleteTaskById(taskId);
                httpStatus = HTTP_STATUS_OK;
            }
        } else {
            System.out.println("올바르지 않은 경로 요청");
            httpStatus = HTTP_STATUS_NOT_FOUND;
        }

        exchange.sendResponseHeaders(httpStatus, responseBody.getBytes(StandardCharsets.UTF_8).length);
        outputStream.write(responseBody.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 모든 Task 목록을 JSON 형식으로 반환한다.
     * */
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    /**
     * 하나의 Task 객체를 JSON 형식으로 반환한다.
     * */
    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    /**
     * 요청받은 할 일을 Task 객체로 변환하여 반환한다.
     * */
    private TaskDto toTask(String content) {
        System.out.println("content = " + content);
        Pattern pattern = Pattern.compile("\\{[\\n\\t\\s]*\"title\"[\\n\\t\\s]*:[\\n\\t\\s]*\"(.*?)\"[\\n\\t\\s]*}");
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return new TaskDto(matcher.group(1));
        } else {
            throw new IllegalArgumentException("not JSON format");
        }
    }

    /**
     * 요청받은 JSON 형식의 content를 문자열로 반환한다.
     * */
    private String getRequestBody(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 증가된 id를 반환한다.
     * */
    private Long generateId() {
        if (tasks.isEmpty() || tasks == null) {
            return 1L;
        }

        Long maxId = Long.MIN_VALUE;
        for (Task task : tasks) {
            maxId = task.getId() > maxId ? task.getId() : maxId;
        }
        return maxId + 1L;
    }

    /**
     * 새로운 할 일을 추가한다.
     * */
    private Task addTask(TaskDto taskDto) {
        Task newTask = taskDto.toTask(generateId());
        tasks.add(newTask);
        return newTask;
    }

    /**
     * id로 할 일을 찾아 반환한다.
     * */
    private Task findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 할 일의 제목을 수정한다.
     * */
    private Task updateTaskById(Long id, String title) {
        return findTaskById(id).updateTitle(title);
    }

    /**
     * id로 할 일을 삭제한다.
     * */
    private void deleteTaskById(Long id) {
        tasks.remove(findTaskById(id));
    }
}
