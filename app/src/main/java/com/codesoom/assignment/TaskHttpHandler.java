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
        }

        if(method.equals("GET") && path.startsWith("/tasks/")){
            String searchIDString = path.substring("/tasks/".length());
            System.out.println("ID String : " + searchIDString);
            Long searchID = new Long(-1);
            try{
                searchID = Long.parseLong(searchIDString);
            }catch (Exception e){
                System.out.println("Not Valid ID");
                returnCode = 404;
                content = "Not Valid ID";
            }

            for(int i=0; i<taskList.size(); i++){
                if(taskList.get(i).equals(searchID)){
                    returnCode = 200;
                    content = taskList.get(i).toString();
                    break;
                }
            }

            if(returnCode == -1){
                returnCode = 404;
                content = "Not Valid ID";
            }
        }
        
        exchange.sendResponseHeaders(returnCode, content.getBytes().length );
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
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
