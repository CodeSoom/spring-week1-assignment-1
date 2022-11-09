package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
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

    private final ObjectMapper objectMapper = new JsonMapper();
    private final OutputStream outputStream = new ByteArrayOutputStream();
    private final List<Task> tasks = new ArrayList<>();

    private static final Integer SC_OK = 200;
    private static final Integer SC_CREATED = 201;
    private static final Integer SC_BADREQUEST = 400;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String content = "";
        String requestMethod = exchange.getRequestMethod(); //GET, POST, PUT/PATCH, DELETE...
        String uri = exchange.getRequestURI().getPath();
        //TODO 어떻게 하면 /posts/{id} 에서 {id}부분만 가져올 수 있는지 찾기
        String[] path = uri.split("/");
        String resource = path[path.length - 1];

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // GET /tasks
        if (requestMethod.equals(GET.name())) {
            content = tasksToJson();
        }

        // POST /tasks
        if (requestMethod.equals(POST.name()) && !body.isBlank()) {
            Task task = contentToTask(body);
            tasks.add(task);
            content = tasksToJson();
        }

        // PUT/PATCH /tasks/{id}
        if (requestMethod.equals(PUT.name()) || requestMethod.equals(PATCH.name())) {
            if (!resource.isBlank() && !body.isBlank()) {
//                Task task = tasks.get(Integer.parseInt(resource));
//                task = contentToTask(body);
            }
        }

        // DELETE /tasks/{id}
        if (requestMethod.equals(DELETE.name())) {
            if (!resource.isBlank()) {
                tasks.remove(Integer.parseInt(resource)-1);
            }
        }

        exchange.sendResponseHeaders(SC_OK, content.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    /**
     * 넘어온 리소스 값으로 Task 필터링하여 반환
     * @param taskId
     * @return 필터링된 Task
     * @throws IOException
     */
    private String getFilteredTask(String taskId) throws IOException {
        //TODO stream filter 찾아보기
        objectMapper.writeValue(outputStream, tasks.stream()
                                                    .filter(t -> Integer.parseInt(taskId) == t.getId()));
        return outputStream.toString();
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
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

}
