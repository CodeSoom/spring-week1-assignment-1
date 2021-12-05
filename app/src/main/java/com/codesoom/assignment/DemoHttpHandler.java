//TODO
//Read - Collection => 완료
//Read - item/element => 완료
//Create => 완료
//Update => 완료
//Delete => 완료

package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>(); //할일 목록
    private static Long taskId = 0L; //저장할 때 사용할 Task의 id

    private final ObjectMapper objectMapper = new ObjectMapper(); //jackson 라이브러리 클래스

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod(); //요청받은 http method
        final URI uri = exchange.getRequestURI();  //요청받은 uri
        final String path = uri.getPath(); //요청받은 path

        //서버 콘솔 출력
        System.out.printf("%s %s%n", method, path);

        //클라이언트에서 받은 요청 처리
        if (path.equals("/")) {
            resolvePathWellcome(exchange);

        } else if (path.equals("/tasks")) {
            resolvePathTasks(exchange, method);

        } else if (path.startsWith("/tasks/")) {
            resolvePathTask(exchange, method, path);
        } else {
            resolvePath404(exchange);
        }
    }

    private void resolvePathWellcome(HttpExchange exchange) throws IOException {
        int code = 200;
        String responseBody = "Hello, World!";
        resolveResponse(exchange, responseBody, code);
    }

    private void resolvePathTasks(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            int code = 200;
            String responseBody = tasksToJSON();
            resolveResponse(exchange, responseBody, code);

        } else if (method.equals("POST")) {
            String json = resolveRequestBody(exchange);

            if (json.isBlank()) {
                int code = 400;
                String responseBody = "Bad Request";
                resolveResponse(exchange, responseBody, code);
                return;
            }

            Task task = insertTask(json);

            int code = 201;
            String responseBody = toJSON(task);
            resolveResponse(exchange, responseBody, code);
        }
    }

    private void resolvePathTask(HttpExchange exchange, String method, String path) throws IOException {
        Long id = null;

        try {
            id = getId(path);
        } catch (NumberFormatException e) {
            int code = 400;
            String responseBody = "Bad Request";
            resolveResponse(exchange, responseBody, code);
            return;
        }

        Optional<Task> findTask = findTaskById(id);

        if (method.equals("GET")) {
            if (findTask.isPresent()) {
                int code = 200;
                String responseBody = toJSON(findTask.get());
                resolveResponse(exchange, responseBody, code);
                return;
            }

        } else if ("PATCH, PUT".contains(method)) {
            String json = resolveRequestBody(exchange);

            if (json.isBlank()) {
                int code = 400;
                String responseBody = "Bad Request";
                resolveResponse(exchange, responseBody, code);
                return;
            }

            if (findTask.isPresent()) {
                Task task = findTask.get();
                updateTask(task, json);

                int code = 200;
                String responseBody = toJSON(task);
                resolveResponse(exchange, responseBody, code);
                return;
            }

        } else if (method.equals("DELETE")) {
            if (findTask.isPresent()) {
                deleteTask(findTask.get());

                int code = 200;
                String responseBody = "";
                resolveResponse(exchange, responseBody, code);
                return;
            }
        }

        int code = 500;
        String responseBody = "Internal Server Error";
        resolveResponse(exchange, responseBody, code);
    }

    private void resolvePath404(HttpExchange exchange) throws IOException {
        int code = 404;
        String responseBody = "Not Found";
        resolveResponse(exchange, responseBody, code);
    }

    /**
     * 클라이언트의 요청 body를 해결해 주는 메서드
     * @param exchange 요청 파라미터를 얻을 수 있는 HttpExchange 객체
     * @return 요청 파라미터 문자열
     */
    private String resolveRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader)
                .lines()
                .collect(Collectors.joining("\n")); //요청받은 body
    }

    //==편의 메서드==//

    /**
     * 클라이언트에서 받은 path에서 Task의 id 얻기
     * @param path http의 path 문자열
     * @return Task의 id
     */
    private Long getId(String path) {
        String[] pathNames = path.split("/");
        return Long.valueOf(pathNames[2]);
    }

    /**
     * 클라이언트에 응답을 해결해 줄 메서드
     * @param exchange 클라이언트에 응답을 보낼 수 있는 HttpExchange 객체
     * @param responseBodyParam 클라이언트에 응답을 보낼 body
     * @param code 클라이언트에 응답할 상태코드
     */
    private void resolveResponse(HttpExchange exchange, String responseBodyParam, int code) throws IOException {
        byte[] responseBody = responseBodyParam.getBytes();
        int responseBodyLength = responseBody.length;

        exchange.sendResponseHeaders(code, responseBodyLength);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody);
        outputStream.flush();
        outputStream.close();
    }

    //==json 변환 메서드==//

    /**
     * Task를 JSON으로 포맷하기
     * @param object json으로 포맷할 객체
     * @return 객체를 json으로 포맷한 문자열
     */
    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

    /**
     * json을 Task로 포맷하기
     * @param json json 문자열
     * @return Task 객체
     */
    private Task toTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    /**
     * 할일(Task) 목록에서 주어인 아이디를 가진 할일(Task)을 찾기
     * @param id 조회할 Task의 id
     * @return Optional의 Task 객체
     */
    private Optional<Task> findTaskById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return Optional.of(task);
            }
        }

        return Optional.empty();
    }

    /**
     * Task 컬렉션을 json으로 포맷하기
     * @return 할일 목록을 json으로 포맷한 문자열
     */
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    //==CRUD 메서드==//

    /**
     * Task 저장
     * @param json json 문자열
     * @return 저장된 Task 객체
     */
    private Task insertTask(String json) throws JsonProcessingException {
        Task task = toTask(json);
        taskId++;
        task.setId(taskId);

        tasks.add(task);

        return task;
    }

    /**
     * Task 수정
     * @param task 수정할 Task 객체
     * @param json 클라이언트에게 받은 요청 json 파라미터 문자열
     */
    private void updateTask(Task task, String json) throws JsonProcessingException {
        Task source = toTask(json);
        task.setTitle(source.getTitle());
    }

    /**
     * Task 삭제
     * @param task 삭제할 Task 객체
     */
    private void deleteTask(Task task) {
        tasks.remove(task);
    }
}
