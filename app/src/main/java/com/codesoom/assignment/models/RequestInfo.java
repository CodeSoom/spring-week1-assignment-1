package com.codesoom.assignment.models;

import com.codesoom.assignment.enums.Command;
import com.codesoom.assignment.exception.BadRequestException;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.stream.Collectors;

/**
 * 1. 클래스명 수정 ReqInfo > RequestInfo
 *  - 아주 명확한게 아니라면 클래스명을 줄여쓰는 것은 지양. 단, 언어에 따라 관습적으로 클래스 명을 줄여쓰는 경우도 있음.
 */
public class RequestInfo {

    private Command command;

    private Long id = null;

    private String body;

    private static int LOCATION_OF_ID = 2;

    public RequestInfo(HttpExchange exchange){
        validationPathSegments(exchange);
        requestAnalysis(exchange);
        resetBody(exchange);
    }

    private void requestAnalysis(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.split("/");

        this.id = extractId(pathSegments);
        this.command = resetCommand(method);

    }

    private Command resetCommand(String method){

        if("GET".equals(method)){
            if(id != null){
                return Command.GET_TASK_DETAIL;
            }
            return Command.GET_TASK_LIST;

        }

        if("POST".equals(method)){
            return Command.CREATE_TASK;
        }

        // 제목 수정하기 - PUT/PATCH /tasks/{id}
        if(("PUT".equals(method) || "PATCH".equals(method)) && id != null){
            return Command.UPDATE_TASK;
        }

        // 삭제하기 - DELETE /tasks/{id}
        if("DELETE".equals(method) && id != null){
            return Command.DELETE_TASK;
        }

        throw new BadRequestException();
    }

    private void validationPathSegments(HttpExchange exchange){

        String path = exchange.getRequestURI().getPath();
        String firstSegment = path.split("/")[1];
        if(!"tasks".equals(firstSegment)){
            throw new BadRequestException();
        }
    }

    public boolean isGetTaskList(){
        if(command == Command.GET_TASK_LIST){
            return true;
        }
        return false;
    }

    public boolean isGetTaskDetail(){
        if(command == Command.GET_TASK_DETAIL){
            return true;
        }
        return false;
    }

    public boolean isCreateTask(){
        if(command == Command.CREATE_TASK){
            return true;
        }
        return false;
    }
    public boolean isUpdateTask(){
        if(command == Command.UPDATE_TASK){
            return true;
        }
        return false;
    }
    public boolean isDeleteTask(){
        if(command == Command.DELETE_TASK){
            return true;
        }
        return false;
    }

    private void resetBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody(); //요청 값을 읽을 수 있는 Stream 반환.
        this.body = new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .collect(Collectors.joining("\n"));
    }


    public Long extractId(String[] pathSegments){

        if(pathSegments.length == 3){
            return Long.parseLong(pathSegments[LOCATION_OF_ID]);
        }
        return null;
    }

    public String getBody(){ return body; }
    public Command getCommand() {return command; }

    public Long getId() {return id;}

}
