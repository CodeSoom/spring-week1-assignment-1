package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {


    private Map<Long,Task> taskMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private int statusCode;

    private String content;

    private Long taskId = 1L;


    @Override
    public void handle(HttpExchange exchange) throws IOException {


            //read request
            String method = exchange.getRequestMethod();
            URI requestURI = exchange.getRequestURI();
            String[] path = requestURI.getPath().substring(1).split("/");
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines()
                    .collect(Collectors.joining("\n"));



            //handle request
            content = " ";

            if(path.length > 0 && path[0].equals("tasks")) {

                if(method.equals("GET")){
                     readTODO(path);
                }else if(method.equals("POST")){
                    createTODO(body);
                }else if(method.equals("PUT") || method.equals("PATCH")){
                    updateTODO(path,body);
                }else if(method.equals("DELETE")){
                    deleteTODO(path);
                }else{
                    statusCode = 400;
                }

            }else{
                statusCode = 404;
            }

            //write response
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());

    }

    //read
    private void readTODO(String[] path) throws IOException {

        if(path.length == 1){         //리스트 조회하기 - GET /tasks
            content = taskMapToJSON();
            statusCode = 200;

        }else{                        //상세 조회하기 - GET /tasks/{id}
            Task task = getTask(getPathVariable(path));

            if(task != null){
                content = taskToJSON(task);
                statusCode = 200;
            }else{
                statusCode = 404;
            }

        }

    }


    //create
    private void createTODO(String body) throws IOException {

        if(!body.isEmpty()){
            Task task = toTask(body);
            task.setId(taskId++);
            taskMap.put(task.getId(), task);

            content = taskToJSON(task);
            statusCode = 201;

        }else{
            statusCode = 400;
        }


    }


    //update
    private void updateTODO(String[] path, String body) throws IOException {

        Task task = getTask(getPathVariable(path));

        if(task != null) {

            if (!body.isEmpty()) {
                task.setTitle(toTask(body).getTitle());
            }

            content = taskToJSON(task);
            statusCode = 200;
        }else{
            statusCode = 404;
        }

    }


    //delete
    private void deleteTODO(String[] path) throws IOException {

        Task task = getTask(getPathVariable(path));

        if (task != null) {
            taskMap.remove(task.getId());
            statusCode = 204;
        }else{
            statusCode = 404;
        }
    }


    //get task by id
    private Task getTask(String pathVariable){

        Task task = null;

        if(pathVariable != null){
            Long id = Long.parseLong(pathVariable);
            if(taskMap.containsKey(id)){
                task = taskMap.get(id);
            }
        }

        return task;
    }


    //get path variable id
    private String getPathVariable(String[] path){

        String pathVariable = null;

        if (path[1].matches("^[0-9]+$")) {
            pathVariable = path[1];
        }

        return pathVariable;
    }


    //string -> task
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


    //tasks -> JSON
    private String taskMapToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream,new ArrayList<>(taskMap.values()));
        return outputStream.toString();
    }


    //task -> JSON
    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream,task);
        return outputStream.toString();
    }


}
