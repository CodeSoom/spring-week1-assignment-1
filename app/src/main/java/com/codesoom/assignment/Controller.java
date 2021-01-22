package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class Controller {
    Service service = new Service();

    public void requestForGet(String path, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        if(path.equals("/tasks")) {
            List<Task> tasks = service.getAllTasks();
            content = (tasks == null ) ? "[]" : service.tasksToJson(tasks);
            exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task getTask = service.getTask(idValue);

        if(getTask == null) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        content = service.taskToJson(getTask);
        exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForPost(String path, String body, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        String requestTitle = body.split("\"")[1];

        if(!requestTitle.equals("title")) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Task task = service.jsonToTask(body);
        service.createTask(task);
        content = service.taskToJson(task);
        exchange.sendResponseHeaders(HttpStatus.CREATED.getHttpStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForPutOrPatch(String path, String body, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task updateTask = service.getTask(idValue);

        if(updateTask == null) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        Task task = service.jsonToTask(body);
        service.updateTask(updateTask, task.getTitle());
        content = service.taskToJson(updateTask);
        exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForDelete(String path, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task deleteTask = service.getTask(idValue);

        if(deleteTask==null) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        service.deleteTask(deleteTask);
        content = "";
        exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getHttpStatus(),0);
        service.writeContentWithOutputStream(exchange, content);
    }
}
