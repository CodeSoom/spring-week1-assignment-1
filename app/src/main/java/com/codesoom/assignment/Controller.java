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
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            if(path.equals("/tasks")) {
                List<Task> tasks = serviceImpl.getAllTasks();
                content = (tasks == null ) ? "[]" : serviceImpl.tasksToJson(tasks);
                exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
                serviceImpl.writeContentWithOutputStream(exchange, content);
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task getTask = serviceImpl.getTask(idValue);

            if(getTask == null) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            content = serviceImpl.taskToJson(getTask);
            exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
            serviceImpl.writeContentWithOutputStream(exchange, content);
        }

        else if(method.equals("POST")){
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            String requestTitle = body.split("\"")[1];

            if(!requestTitle.equals("title")) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            Task task = serviceImpl.jsonToTask(body);
            serviceImpl.createTask(task);
            content = serviceImpl.taskToJson(task);
            exchange.sendResponseHeaders(HttpStatus.CREATED.getHttpStatus(),content.getBytes().length);
            serviceImpl.writeContentWithOutputStream(exchange, content);
        }

        else if(method.equals("PUT") || method.equals("PATCH")) {
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task updateTask = serviceImpl.getTask(idValue);

            if(updateTask == null) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
                serviceImpl.writeContentWithOutputStream(exchange, content);
                return;
            }

            Task task = serviceImpl.jsonToTask(body);
            serviceImpl.updateTask(updateTask, task.getTitle());
            content = serviceImpl.taskToJson(updateTask);
            exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
            serviceImpl.writeContentWithOutputStream(exchange, content);
        }

        else if(method.equals("DELETE")) {
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
                serviceImpl.writeContentWithOutputStream(exchange, "");
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task deleteTask = serviceImpl.getTask(idValue);

            if(deleteTask==null) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
                serviceImpl.writeContentWithOutputStream(exchange, content);
                return;
            }

            serviceImpl.deleteTask(deleteTask);
            content = "";
            exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getHttpStatus(),0);
            serviceImpl.writeContentWithOutputStream(exchange, content);
        }
    }


//    public void requestForGet(String path, HttpExchange exchange) throws IOException {
//        String content= "";
//        if(!path.startsWith("/tasks")) {
//            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        if(path.equals("/tasks")) {
//            List<Task> tasks = serviceImpl.getAllTasks();
//            content = (tasks == null ) ? "[]" : serviceImpl.tasksToJson(tasks);
//            exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
//            serviceImpl.writeContentWithOutputStream(exchange, content);
//            return;
//        }
//
//        Long idValue = Long.parseLong(path.substring(7));
//        Task getTask = serviceImpl.getTask(idValue);
//
//        if(getTask == null) {
//            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        content = serviceImpl.taskToJson(getTask);
//        exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
//        serviceImpl.writeContentWithOutputStream(exchange, content);
//    }
//
//    public void requestForPost(String path, String body, HttpExchange exchange) throws IOException {
//        String content = "";
//        if(!path.startsWith("/tasks")) {
//            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        String requestTitle = body.split("\"")[1];
//
//        if(!requestTitle.equals("title")) {
//            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        Task task = serviceImpl.jsonToTask(body);
//        serviceImpl.createTask(task);
//        content = serviceImpl.taskToJson(task);
//        exchange.sendResponseHeaders(HttpStatus.CREATED.getHttpStatus(),content.getBytes().length);
//        serviceImpl.writeContentWithOutputStream(exchange, content);
//    }
//
//    public void requestForPutOrPatch(String path, String body, HttpExchange exchange) throws IOException {
//        String content = "";
//        if(!path.startsWith("/tasks")) {
//            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        Long idValue = Long.parseLong(path.substring(7));
//        Task updateTask = serviceImpl.getTask(idValue);
//
//        if(updateTask == null) {
//            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
//            serviceImpl.writeContentWithOutputStream(exchange, content);
//            return;
//        }
//
//        Task task = serviceImpl.jsonToTask(body);
//        serviceImpl.updateTask(updateTask, task.getTitle());
//        content = serviceImpl.taskToJson(updateTask);
//        exchange.sendResponseHeaders(HttpStatus.OK.getHttpStatus(),content.getBytes().length);
//        serviceImpl.writeContentWithOutputStream(exchange, content);
//    }
//
//    public void requestForDelete(String path, HttpExchange exchange) throws IOException {
//        String content = "";
//        if(!path.startsWith("/tasks")) {
//            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getHttpStatus(),0);
//            serviceImpl.writeContentWithOutputStream(exchange, "");
//            return;
//        }
//
//        Long idValue = Long.parseLong(path.substring(7));
//        Task deleteTask = serviceImpl.getTask(idValue);
//
//        if(deleteTask==null) {
//            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getHttpStatus(), 0);
//            serviceImpl.writeContentWithOutputStream(exchange, content);
//            return;
//        }
//
//        serviceImpl.deleteTask(deleteTask);
//        content = "";
//        exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getHttpStatus(),0);
//        serviceImpl.writeContentWithOutputStream(exchange, content);
//    }


}
