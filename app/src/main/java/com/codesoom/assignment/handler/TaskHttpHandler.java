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
import java.util.Optional;
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

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String content = "";
        String requestMethod = exchange.getRequestMethod(); //GET, POST, PUT/PATCH, DELETE...
        String uri = exchange.getRequestURI().getPath();
        String[] path = uri.split("/");
        String resource = path[path.length - 1];

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

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
        }

        // GET /tasks/{id}
        if (requestMethod.equals(GET.name())) {
            System.out.println("TaskHttpHandler.handle");
        }

        // PUT/PATCH /tasks/{id}
        if (requestMethod.equals(PUT.name()) || requestMethod.equals(PATCH.name())) {
            if (!resource.isBlank() && !body.isBlank()) {
                Task task = contentToTask(body);
                handleUpdate(exchange, task, body);
            } else {
                handleError(exchange, content);
            }
        }

        // DELETE /tasks/{id}
        if (requestMethod.equals(DELETE.name())) {
            if (!resource.isBlank()) {
                tasks.remove(Integer.parseInt(resource)-1);
                handleGet(exchange, content);
            }
        }
    }

    private void handleGet(HttpExchange exchange, String content) throws IOException {
        new ResponseSuccess(exchange).sendResponse(content);
    }

    private void handleCreate(HttpExchange exchange, String body) throws IOException {
        Task task = contentToTask(body);
        tasks.add(task);

        new ResponseCreated(exchange).sendResponse(tasksToJson());
    }

    private void handleUpdate(HttpExchange exchange, Task task, String body) throws IOException {
        Task source = contentToTask(body);
        source.setTitle(task.getTitle());

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
    private Optional<Task> getFilteredTask(String taskId) throws IOException {
        System.out.println("TaskHttpHandler.getFilteredTask");
        int id = Integer.parseInt(taskId);

        //TODO stream filter 찾아보기
        Optional<Task> task = tasks.stream()
                                    .filter(t -> t.getId() == id)
                                    .findFirst();
       return task;
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
        OutputStream os = new ByteArrayOutputStream();
        objectMapper.writeValue(os, tasks);
        return os.toString();
    }

}
