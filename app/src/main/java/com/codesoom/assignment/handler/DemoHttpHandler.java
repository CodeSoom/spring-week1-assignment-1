package com.codesoom.assignment.handler;

import com.codesoom.assignment.enums.Command;
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

    private Long TASKS_ID = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try{
            RequestInfo requestInfo = new RequestInfo(exchange);        // HttpExchange에 대한 요청 객체 생성
            Command command = requestInfo.getCommand();
            Long id = requestInfo.getId();

            // task 리스트 조회
            if(command == Command.GET_TASK_LIST) {

                ResponseProcess(exchange, objectToJSON(tasks.values()), 200);
                return;
            }

            // task 상세 조회
            if(command == Command.GET_TASK_DETAIL){

                if(!existIdInTasks(id)){
                    ResponseProcess(exchange, "", 404);
                    return;
                }

                Task task = tasks.get(id);
                ResponseProcess(exchange, objectToJSON(task), 200);
                return;
            }

            // task 생성
            if(command == Command.CREATE_TASK){

                Task task = toTask(requestInfo.getBody());
                increaseTaskId();
                task.setId(TASKS_ID);
                tasks.put(TASKS_ID,task);
                ResponseProcess(exchange, objectToJSON(task), 201);
                return;
            }

            // task 수정
            if(command == Command.UPDATE_TASK){

                if(!existIdInTasks(id)){
                    ResponseProcess(exchange, "", 404);
                    return;
                }

                Task task = toTask(requestInfo.getBody());
                task.setId(id);
                tasks.put(id,task); // 덮어쓰기
                ResponseProcess(exchange, objectToJSON(task), 200);
                return;
            }

            // task 삭제
            if(command == Command.DELETE_TASK){

                if(!existIdInTasks(id)){
                    ResponseProcess(exchange,"",404);
                    return;
                }

                tasks.remove(id); // 삭제
                ResponseProcess(exchange, "",204);
                return;
            }

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

    /**
     * TaskId 증가
     */
    private void increaseTaskId(){
        TASKS_ID = TASKS_ID + 1;
    }

    /**
     * tasks에 존재하는 id인지 확인
     */
    private boolean existIdInTasks(Long id){
        return tasks.containsKey(id);
    }

}

