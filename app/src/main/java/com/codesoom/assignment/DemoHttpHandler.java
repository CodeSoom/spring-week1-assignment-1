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
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();

    // 상태 코드 정의
    int HTTP_STATUS_OK = 200;
    int HTTP_STATUS_CREATED = 201;
    int HTTP_STATUS_CREATED_NO_CONTENT = 204;
    int HTTP_STATUS_NOT_FOUND = 404;

    String NOT_FOUND_MSG = "Cannot find task by id";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        System.out.println(method + " " + path);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // path가 아이디를 가지는지 여부
        boolean hasTaskId = Pattern.matches("/tasks/[0-9]+$", path);



        // 콘텐츠 초기화
        String content = "";

        // 아래 path 조건에 해당이 안되면 잘못된 요청이므로 404로 초기화
        int statusCode = 404;

        // GET /tasks
        if ("GET".equals(method) && "/tasks".equals(path)) {
            content = tasksToJson(tasks);
            statusCode = HTTP_STATUS_OK;
        }

        // GET /tasks/{id}
        if ("GET".equals(method) && hasTaskId) {
            Long id = extractIdFromPath(path);
            Task findTask = findTaskById(id);
            if (findTask == null) {
                content = NOT_FOUND_MSG;
                statusCode = HTTP_STATUS_NOT_FOUND;
            } else {
                content = taskToJson(findTask);
                statusCode = HTTP_STATUS_OK;
            }
        }

        // POST /tasks
        if ("POST".equals(method) && "/tasks".equals(path)) {
            Task task = createTaskWithId(body);
            content = taskToJson(task);
            tasks.add(task);

            statusCode = HTTP_STATUS_CREATED;
        }

        // Delete /tasks/{id}
        if ("DELETE".equals(method) && hasTaskId) {
            Long id = extractIdFromPath(path);
            Task findTask = findTaskById(id);

            if (findTask == null) {
                content = NOT_FOUND_MSG;
                statusCode = HTTP_STATUS_NOT_FOUND;
            } else {
                tasks.remove(findTask);
                content = tasksToJson(tasks);
                statusCode = HTTP_STATUS_CREATED_NO_CONTENT;
            }
        }

        // PUT,PATCH /tasks/{id}
        // 여기서는 Task 객체의 변경 가능한 필드가 title 뿐이므로 put, patch가 동일하게 동작한다.
        if (("PATCH".equals(method) || "PUT".equals(method)) && hasTaskId) {
            Long id = extractIdFromPath(path);
            Task findTask = findTaskById(id);
            if (findTask == null) {
                content = NOT_FOUND_MSG;
                statusCode = HTTP_STATUS_NOT_FOUND;
            } else {
                Task inputTask = toTask(body);
                findTask.setTitle(inputTask.getTitle());

                content = taskToJson(findTask);
                statusCode = HTTP_STATUS_OK;
            }
        }

        // 상태코드가 204이면 콘텐츠 길이를 -1로 해준다. -1이 아니면 경고 메시지를 출력하고 강제로 -1로 변환됨
        long contentLength = statusCode == 204 ? -1L : content.getBytes().length;

        exchange.sendResponseHeaders(statusCode, contentLength);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    /**
     * http 요청의 body 문자열을 받아 할 일 객체를 생성해 리턴합니다.
     * <p> body 문자열은 반드시 json 형식이어야 합니다. </p>
     *
     * @param body http 요청의 body 문자열
     * @return Task 할 일
     * @throws JsonProcessingException json 처리 중 문제가 발생한 경우
     */
    private Task createTaskWithId(String body) throws JsonProcessingException {
        Task task;
        task = toTask(body);
        if (tasks.size() == 0) {
            task.setId(1L);
        } else {
            long maxId = tasks
                    .stream()
                    .mapToLong(Task::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
            task.setId(maxId + 1L);
        }

        return task;
    }

    /**
     * 식별자 아이디로 할 일 객체를 찾아서 리턴합니다.
     *
     * @param id 식별자 id
     * @return Task 할 일
     */
    private Task findTaskById(Long id) {
        return tasks
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * 주어진 path에서 식별자 숫자를 찾아 리턴합니다.
     *
     * @param path path url 경로
     * @return Id 식별자 숫자
     */
    private Long extractIdFromPath(String path) {
        String[] pathSplit = path.split("/");
        String id = pathSplit[pathSplit.length - 1];
        return Long.parseLong(id);
    }

    /**
     * Json을 Task 객체로 변환
     *
     * @param content 콘텐츠
     * @return Task 객체
     * @throws JsonProcessingException
     */
    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content, Task.class);
    }

    /**
     * Task 객체 리스트를 Json으로 변환후 문자열로 리턴합니다.
     *
     * @param tasks 할 일 객체 리스트
     * @return String Json으로 변환된 객체
     * @throws IOException stream 처리 중 예외가 발생한 경우
     */
    private String tasksToJson(List<Task> tasks) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    /**
     * Task 객체를 Json으로 변환후 문자열로 리턴합니다.
     *
     * @param task 할 일 객체
     * @return String Json으로 변환된 객체
     * @throws IOException stream 처리 중 예외가 발생한 경우
     */
    private String taskToJson(Task task) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
