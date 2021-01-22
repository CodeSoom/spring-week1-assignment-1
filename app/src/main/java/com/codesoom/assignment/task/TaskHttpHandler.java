package com.codesoom.assignment.task;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static com.codesoom.assignment.http.HttpStatus.*;


public class TaskHttpHandler implements HttpHandler {

    private TaskController taskcontroller = new TaskController();
    private TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        String uri = httpExchange.getRequestURI().getPath();

        if (uri.equals("/")){
            httpExchange.sendResponseHeaders(OK.getStatus(), 0);
            taskService.processBody(httpExchange, "");
            return;
        }
        if (!uri.startsWith("/tasks")){
            httpExchange.sendResponseHeaders(NOT_FOUND.getStatus(), 0);
            taskService.processBody(httpExchange, "");
            return;
        }

        switch (method){
            case "GET" :
                taskcontroller.getController(httpExchange);
            case "POST" :
                taskcontroller.postController(httpExchange);
            case "PUT" :
                taskcontroller.putController(httpExchange);
            case "PATCH" :
                taskcontroller.patchController(httpExchange);
            case "DELETE" :
                taskcontroller.deleteController(httpExchange);
            default:
                httpExchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatus(), 0);
        }

    }





}
