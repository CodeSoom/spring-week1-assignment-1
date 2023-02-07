package com.codesoom.assignment.handler;

import com.codesoom.assignment.models.ReqInfo;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jdk.jshell.spi.ExecutionControlProvider;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Long,Task> tasks = new HashMap<>();

    private Long taskId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ReqInfo reqInfo = new ReqInfo(exchange);       // HttpExchange에 대한 요청 객체 생성
        String method = reqInfo.getMethod();           // 메서드 추출
        String path = reqInfo.extractPathValue(1);      // 첫번째 path 추출
        String path2 = reqInfo.extractPathValue(2);     // 두번째 path 추출
        String reqBody = reqInfo.getBody();            // 요청 바디 추출
        String resBody = "";

        // 목록얻기 - GET /tasks
        if("GET".equals(method) && path.equals("tasks") && path2 == null){

            resBody = tasksToJSON();                        // tasks를 Json String 으로 변환
            returnResBody(exchange, resBody, 200);  // 응답처리
            return;
        }

        // 상세 조회하기 - GET /tasks/{id}
        if("GET".equals(method) && path.equals("tasks") && path2 != null){

            Long id = Long.parseLong(path2);                // path2 값을 Long으로 변환 (id 추출)
            Task task = tasks.get(id);
            if(task == null){
                returnResBody(exchange, "", 404);  // 응답처리
                return;
            }
            resBody = taskToJSON(task);                     // task를 Json String 으로 변환
            returnResBody(exchange, resBody, 200);  // 응답처리
            return;
        }

        // 생성하기 - POST /tasks
        if("POST".equals(method) && "tasks".equals(path)){

            Task task = toTask(reqBody);                    // 요청 Json String 값을 Task 객체로 변환
            task.setId(taskId);                           // task 객체 ID 값 셋팅
            tasks.put(taskId,task);
            taskId++;
            returnResBody(exchange, "", 201);
            return;
        }

        // 제목 수정하기 - PUT/PATCH /tasks/{id}
        if(("PUT".equals(method) || "PATCH".equals(method)) && "tasks".equals(path) && path2 != null){

            Long id = Long.parseLong(path2);                // path2 값을 Long으로 변환 (id 추출)
            Task task = tasks.get(id);
            if(task == null){
                returnResBody(exchange, "", 404);  // 응답처리
                return;
            }
            Task updateTask = toTask(reqBody);                    // 요청 Json String 값을 Task 객체로 변환
            updateTask.setId(id);
            tasks.put(id,updateTask);
            resBody = taskToJSON(updateTask);
            returnResBody(exchange, resBody, 200);
            return;
        }

        // 삭제하기 - DELETE /tasks/{id}
        if("DELETE".equals(method) && "tasks".equals(path) && path2 != null){

            Long id = Long.parseLong(path2);                // path2 값을 Long으로 변환 (id 추출)
            Task task = tasks.get(id);
            if(task == null){
                returnResBody(exchange, "", 404);  // 응답처리
                return;
            }
            tasks.remove(id);
            returnResBody(exchange, "", 200);
            return;
        }

        returnResBody(exchange, "Bad Request !!",  400);
    }

    /**
     *
     * @param content
     * @return Task
     * @throws IOException
     */
    private Task toTask(String content) throws IOException {
        try{
            return objectMapper.readValue(content, Task.class);
        }catch(Exception e){
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * @throws IOException
     * @Desc tasks의 Value 값을 JSON 문자열로 리턴한다.
     */
    private String tasksToJSON() throws IOException {

        // tasks의 value 값들에 대한 Task Collection 추출
        Collection<Task> tasksValues = tasks.values();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasksValues);

        return outputStream.toString();

    }

    private String taskToJSON(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();

    }

    private void returnResBody(HttpExchange exchange, String resBody, int resCode) {
        try {
            exchange.sendResponseHeaders(resCode, resBody.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(resBody.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

