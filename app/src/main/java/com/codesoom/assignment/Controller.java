package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class Controller {
    Service service = new Service();

    public void requestForGet(String path, List<Task> tasks, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        if(path.equals("/tasks")) {
            content = (tasks == null ) ? "[]" : service.tasksToJson(tasks);
            exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task getTask = service.getTask(idValue, tasks);

        if(getTask == null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        content = service.taskToJson(getTask);
        exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForPost(String path, String body, List<Task> tasks, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        String requestTitle = body.split("\"")[1];

        if(!requestTitle.equals("title")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Task task = service.jsonToTask(body);
        service.createTask(task, tasks);
        content = service.taskToJson(task);
        exchange.sendResponseHeaders(Status.CREATED.getStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForPutOrPatch(String path, String body, List<Task> tasks, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task updateTask = service.getTask(idValue, tasks);

        if(updateTask == null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(), 0);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        Task task = service.jsonToTask(body);
        service.updateTask(updateTask, task.getTitle());
        content = service.taskToJson(updateTask);
        exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
        service.writeContentWithOutputStream(exchange, content);
    }

    public void requestForDelete(String path, List<Task> tasks, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            service.writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task deleteTask = service.getTask(idValue, tasks);

        if(deleteTask==null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(), 0);
            service.writeContentWithOutputStream(exchange, content);
            return;
        }

        service.deleteTask(deleteTask, tasks);
        content = "";
        exchange.sendResponseHeaders(Status.NO_CONTENT.getStatus(),0);
        service.writeContentWithOutputStream(exchange, content);
    }
}
