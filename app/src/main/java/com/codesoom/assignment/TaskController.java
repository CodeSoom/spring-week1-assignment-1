package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpStatus.*;

public class TaskController {

    ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long count = 0L;

    public void getController(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());
        String body="";

        if (uri.length() <= 7){
            body = tasksToJSON();
            httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
        } else{
            int id = getId(uri);
            if (tasks.size() <= 0 || id > tasks.size()){
                httpExchange.sendResponseHeaders(INTERNAL_SERVER_ERROR.getStatus(), 0);
            }else{
                body = tasksToJSONById(tasks.get(id-1));
                httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
            }
        }

        //set request body
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }



    public void postController(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        System.out.println(body);
        Task task = toTask(body);
        count += 1;
        task.setId(count);
        System.out.println(task);

        tasks.add(task);

        httpExchange.sendResponseHeaders(CREATED.getStatus(), 0);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public void putController(HttpExchange httpExchange) {
    }

    public void patchController(HttpExchange httpExchange) {
    }

    public void deleteController(HttpExchange httpExchange) {
    }

    private int getId(String uri) {
        return Integer.parseInt(uri.split("/")[2]);
    }

    public String getBody(HttpExchange httpExchange){
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String tasksToJSONById(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

}
