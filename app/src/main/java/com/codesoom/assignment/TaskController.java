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

    TaskService taskService = new TaskService();

    public void getController(HttpExchange httpExchange) throws IOException {
        String uri = String.valueOf(httpExchange.getRequestURI());

        if (!taskService.isBodyEmpty(httpExchange)){
            errorController(httpExchange);
            return;
        }

        if (uri.length() <= 7){
            String body = taskService.getTasks();
            httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
            taskService.processBody(httpExchange, body);
            return;
        }
        int id = taskService.getId(uri);

        if (id <= taskService.tasks.size() && id >= 1){
            String body = taskService.getTaskById(id);
            httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
            taskService.processBody(httpExchange, body);
            return;
        }
        errorController(httpExchange);

    }

    public void postController(HttpExchange httpExchange) throws IOException {
        String uri = String.valueOf(httpExchange.getRequestURI());

        if (taskService.isBodyEmpty(httpExchange) || uri.length() >= 8){
            errorController(httpExchange);
            return;
        }

        String body = taskService.createTask(httpExchange);
        httpExchange.sendResponseHeaders(CREATED.getStatus(), body.getBytes().length);
        taskService.processBody(httpExchange, body);
    }

    public void putController(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());
        int id = getId(uri);
        System.out.println(id);

        String body = getBody(httpExchange);
        String title = toTask(body).getTitle();

        tasks.get(id-1).updateTitle(title);

        Task task = tasks.get(id-1);
        System.out.println(task);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(task);
        byte[] taskBytes = bos.toByteArray();
        System.out.println(taskBytes);


        httpExchange.sendResponseHeaders(OK.getStatus(), 0);
        OutputStream outputStream = httpExchange.getResponseBody();
//        outputStream.write(objectBytes);
        outputStream.flush();
        outputStream.close();

    }

    public void patchController(HttpExchange httpExchange) {
    }

    public void deleteController(HttpExchange httpExchange) {
    }


    private void errorController(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(INTERNAL_SERVER_ERROR.getStatus(), 0);
        taskService.processBody(httpExchange, "");
    }

}
