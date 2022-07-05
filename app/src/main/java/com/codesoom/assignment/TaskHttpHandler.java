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
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
    private List<Task> taskList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long curTaskID = 0L;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        int returnCode = -1;
        String content = "[]";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(method.equals("GET") && path.equals("/tasks")){
            content = taskToJSON();
            returnCode = 200;
        }

        if(method.equals("GET") && path.startsWith("/tasks/")){
            String searchIDString = path.substring("/tasks/".length());
            System.out.println("ID String : " + searchIDString);
            Long searchID;
            int taskIdx;
            try{
                searchID = Long.parseLong(searchIDString);
                taskIdx = findTaskIdx(searchID);
                if(taskIdx!= -1){
                    returnCode = 200;
                    content = taskList.get(taskIdx).toString();
                }
                else{
                    returnCode = 404;
                    content = "no ID";
                }

            }catch (Exception e){
                System.out.println("Not Valid ID");
                returnCode = 404;
                content = "Not Valid ID";
            }
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            if(!body.isBlank()){
                Task task = makeTask(body);
                task.setId(++curTaskID);
                taskList.add(task);

                returnCode = 200;
                content = task.toString();
            }
        }


        exchange.sendResponseHeaders(returnCode, content.getBytes().length );
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private int findTaskIdx(Long id){

        for(int i=0; i<taskList.size(); i++){
            if(taskList.get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }


    private Task makeTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    //taskList -> JSON으로 변환 후 return
    private String taskToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskList);

        return outputStream.toString();
    }
}
