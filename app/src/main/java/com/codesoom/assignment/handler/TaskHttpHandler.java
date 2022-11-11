package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.response.ResponseCreated;
import com.codesoom.assignment.response.ResponseNotFound;
import com.codesoom.assignment.response.ResponseSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.model.HTTPMethod.DELETE;
import static com.codesoom.assignment.model.HTTPMethod.GET;
import static com.codesoom.assignment.model.HTTPMethod.PATCH;
import static com.codesoom.assignment.model.HTTPMethod.POST;
import static com.codesoom.assignment.model.HTTPMethod.PUT;

/**
 * TODO 11월 첫째주 과제
 * ToDo 목록 얻기 - GET /tasks
 * ToDo 상세 조회하기 - GET /tasks/{id}
 * ToDo 생성하기 - POST /tasks
 * ToDo 제목 수정하기 - PUT/PATCH /tasks/{id}
 * ToDo 삭제하기 - DELETE /tasks/{id}
 */
public class TaskHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new JsonMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String content = "";
        Long taskId = 0L;
        String requestMethod = exchange.getRequestMethod(); //GET, POST, PUT/PATCH, DELETE...
        String uri = exchange.getRequestURI().getPath();
        // A path consists of a sequence of path segments separated by a slash ("/") character.
        String[] pathSegments = uri.split("/");
        String resource = pathSegments[pathSegments.length - 1];

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (isNumeric(resource)) {
            taskId = Long.parseLong(resource);
        }

        // /tasks
        if (!isNumeric(resource)) {
            if (requestMethod.equals(GET.name())) {
                handleGet(exchange, tasksToJson());
            }

            if (requestMethod.equals(POST.name())) {
                if(body.isBlank()) {
                    handleError(exchange, content);
                } else {
                    handleCreate(exchange, body);
                }
            }

            if (requestMethod.equals(PUT.name()) || requestMethod.equals(PATCH.name()) || requestMethod.equals(DELETE.name())) {
                handleError(exchange, content);
            }
        }

        // GET /tasks/{id}
        if (requestMethod.equals(GET.name())) {
            Task filteredTask = getFilteredTask(taskId);
            if (filteredTask != null) {
                handleGetDetail(exchange, filteredTask);
            } else {
                handleError(exchange, content);
            }
        }

        // PUT/PATCH /tasks/{id}
        if (requestMethod.equals(PUT.name()) || requestMethod.equals(PATCH.name())) {
            if (!body.isBlank()) {
                String newTitle = contentToTask(body).getTitle();
                handleUpdate(exchange, taskId, newTitle);
            } else {
                handleError(exchange, content);
            }
        }

        // DELETE /tasks/{id}
        if (requestMethod.equals(DELETE.name())) {
            if (!resource.isBlank()) {
                tasks.remove(getFilteredTask(taskId));
                handleGet(exchange, content);
            } else {
                handleError(exchange, content);
            }
        }
    }

    private void handleGet(HttpExchange exchange, String content) throws IOException {
        new ResponseSuccess(exchange).sendResponse(content);
    }

    private void handleGetDetail(HttpExchange exchange, Task filteredTask) throws IOException {
        new ResponseSuccess(exchange).sendResponse(filteredTask.toString());
    }

    private void handleCreate(HttpExchange exchange, String body) throws IOException {
        Task task = contentToTask(body);
        task.setId(generatedId());
        tasks.add(task);

        new ResponseCreated(exchange).sendResponse(tasksToJson());
    }

    private void handleUpdate(HttpExchange exchange, Long taskId, String newTitle) throws IOException {
        Task filteredTask = getFilteredTask(taskId);
        if (filteredTask == null) {
            new ResponseNotFound(exchange);
        }

        filteredTask.setTitle(newTitle);

        new ResponseSuccess(exchange).sendResponse(tasksToJson());
    }

    private void handleError(HttpExchange exchange, String content) throws IOException {
        new ResponseNotFound(exchange).sendResponse(content);
    }

    /**
     * value값이 int형인지 확인
     * @param value
     * @return true || false
     */
    private boolean isNumeric(String value) {
        return value != null && value.matches("[0-9.]+");
    }

    /**
     * 넘어온 리소스 값으로 Task 필터링하여 반환
     *
     * @param taskId
     * @return 필터링된 Task
     * @throws IOException
     */
    private Task getFilteredTask(Long taskId) {
        //TODO stream filter 찾아보기
       return tasks.stream()
               .filter(t -> t.getId().equals(taskId))
               .findFirst()
               .orElse(null);
    }

    /**
     * requestBody에서 받아온 content를 Task 자바 객체로 변환
     * @param content
     * @return Task 객체
     * @throws JsonProcessingException
     */
    private Task contentToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }
    
    /**
     * 자바 객체인 Task를 Json 형태로 변환
     * @return Json 형태로 변환된 Task
     * @throws IOException
     */
    private String tasksToJson() throws IOException {
        //        objectMapper.writeValue(outputStream, tasks);
        // 새로 Outputstream을 만들어서 리턴해야한다. 이유는 모름 찾아봐야함.
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    /**
     * Task의 id 자동 증가
     * @return 1씩 증가된 id
     */
    private Long generatedId() {
        return id += 1;
    }
}
