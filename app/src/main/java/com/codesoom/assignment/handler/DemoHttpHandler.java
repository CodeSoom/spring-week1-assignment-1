package com.codesoom.assignment.handler;

import com.codesoom.assignment.models.RequestInfo;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.*;

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<Long,Task> tasks = new HashMap<>();

    private Long taskId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try{
            RequestInfo requestInfo = new RequestInfo(exchange);        // HttpExchange에 대한 요청 객체 생성
            String method = requestInfo.getMethod();                    // 메서드 추출
            String reqBody = requestInfo.getBody();                     // 요청 바디 추출
            String path = requestInfo.extractFirstPathSegment();        // 첫번째 세그먼트 추출

            // task 조회 - GET /tasks 또는 GET/tasks/{id}
            if("GET".equals(method) && "tasks".equals(path)){

                // 요청 값에 id 값이 있는지
                if(!requestInfo.existId()){
                    ResponseProcess(exchange, objectToJSON(tasks.values()), 200);  // tasks를 Json String 으로 변환 후 응답처리
                    return;
                }

                Long id = requestInfo.extractId();

                // tasks에 id에 해당하는 값이 있는지
                if(!tasks.containsKey(id)){
                    ResponseProcess(exchange, "", 404);
                    return;
                }

                Task task = tasks.get(id);
                ResponseProcess(exchange, objectToJSON(task), 200);
                return;
            }

            // 생성하기 - POST /tasks
            if("POST".equals(method) && "tasks".equals(path)){

                Task task = toTask(reqBody);
                task.setId(++taskId);
                tasks.put(taskId,task);

                ResponseProcess(exchange, objectToJSON(task), 201);
                return;
            }

            // 제목 수정하기 - PUT/PATCH /tasks/{id}
            if(("PUT".equals(method) || "PATCH".equals(method)) && "tasks".equals(path) && requestInfo.existId()){

                Long id = requestInfo.extractId();

                // tasks에 id에 해당하는 값이 있는지
                if(!tasks.containsKey(id)){
                    ResponseProcess(exchange, "",404);  // 응답처리
                    return;
                }

                Task task = toTask(reqBody);
                task.setId(id);
                tasks.put(id,task); // 덮어쓰기
                ResponseProcess(exchange, objectToJSON(task), 200);
                return;
            }

            // 삭제하기 - DELETE /tasks/{id}
            if("DELETE".equals(method) && "tasks".equals(path) && requestInfo.existId()){

                Long id = requestInfo.extractId();

                // tasks에 id에 해당하는 값이 있는지
                if(!tasks.containsKey(id)){
                    ResponseProcess(exchange,"",404);
                    return;
                }

                tasks.remove(id); // 삭제
                ResponseProcess(exchange, "",204);
                return;
            }

            ResponseProcess(exchange,  "",400);
        }catch(Exception e){
            ResponseProcess(exchange,  "",400);
        }

    }

    /**
     * 문자열을 Task 객체로 변환
     */
    private Task toTask(String content) throws JsonProcessingException {

        return objectMapper.readValue(content, Task.class);

    }


    /**
     * Object를 Json 문자열로 변환
     */
    private String objectToJSON(Object object) throws IOException {

        return objectMapper.writeValueAsString(object);

    }

    /**
     * 응답 처리
     */
    private void ResponseProcess(HttpExchange exchange, String resBody, int resCode) {
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

