package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class Controller {
    ServiceImpl serviceImpl = new ServiceImpl();

    public void requestHttp(String method, String path, String body, HttpExchange exchange) throws IOException {
        String content = "";

        if(method.equals("GET")){
            if(!path.startsWith("/tasks")) {
                serviceImpl.send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            if(path.equals("/tasks")) {
                List<Task> tasks = serviceImpl.getAllTasks();
                content = serviceImpl.taskToJson(tasks);
                serviceImpl.send(exchange, content, HttpStatus.OK.getHttpStatus());
                return;
            }

            Long idValue = Long.parseLong(path.substring("/tasks/".length()));
            Task getTask = serviceImpl.getTask(idValue);

            if(getTask == null) {
                serviceImpl.send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
                return;
            }

            content = serviceImpl.taskToJson(getTask);
            serviceImpl.send(exchange, content, HttpStatus.OK.getHttpStatus());
        }

        else if(method.equals("POST")){
            if(!path.startsWith("/tasks")) {
                serviceImpl.send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            String requestTitle = body.split("\"")[1];
            if(!requestTitle.equals("title")) {
                serviceImpl.send(exchange, content, HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            Task task = serviceImpl.jsonToTask(body);
            serviceImpl.createTask(task);
            content = serviceImpl.taskToJson(task);
            serviceImpl.send(exchange, content, HttpStatus.CREATED.getHttpStatus());
        }

        else if(method.equals("PUT") || method.equals("PATCH")) {
            if(!path.startsWith("/tasks")) {
                serviceImpl.send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            Long idValue = Long.parseLong(path.substring("/tasks/".length()));
            Task updateTask = serviceImpl.getTask(idValue);

            if(updateTask == null) {
                serviceImpl.send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
                return;
            }

            Task task = serviceImpl.jsonToTask(body);
            serviceImpl.updateTask(updateTask, task.getTitle());
            content = serviceImpl.taskToJson(updateTask);
            serviceImpl.send(exchange, content, HttpStatus.OK.getHttpStatus());
        }

        else if(method.equals("DELETE")) {
            if(!path.startsWith("/tasks")) {
                serviceImpl.send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            Long idValue = Long.parseLong(path.substring("/tasks/".length()));
            Task deleteTask = serviceImpl.getTask(idValue);

            if(deleteTask==null) {
                serviceImpl.send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
                return;
            }

            serviceImpl.deleteTask(deleteTask);
            serviceImpl.send(exchange, "", HttpStatus.NO_CONTENT.getHttpStatus());
        }
    }

}
