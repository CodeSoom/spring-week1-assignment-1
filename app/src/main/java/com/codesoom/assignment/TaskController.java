package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.Status.CREATED;
import static com.codesoom.assignment.Status.OK;

public class TaskController {

    ObjectMapper objectMapper = new ObjectMapper();
    private static List<Task> tasks = new ArrayList<>();

    public void getController(HttpExchange httpExchange) throws IOException {
        String body = tasksToJSON();

        httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
        //set request body
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public void postController(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        System.out.println(body);
        Task tempTask = toTask(body);
        Task task = new Task(tempTask.getTitle());
        System.out.println(task.getId());

        tasks.add(task);

        httpExchange.sendResponseHeaders(CREATED.getStatus(), 0);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.close();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    public void putController(HttpExchange httpExchange) {
    }

    public void patchController(HttpExchange httpExchange) {
    }

    public void deleteController(HttpExchange httpExchange) {
    }


    public String getBody(HttpExchange httpExchange){
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }




}
