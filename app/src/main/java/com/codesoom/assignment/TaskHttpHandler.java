package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private TaskController controller = new TaskController();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        switch (method){
            case "GET" :
                controller.getController(httpExchange);
            case "POST" :
                controller.postController(httpExchange);
            case "PUT" :
                controller.putController(httpExchange);
            case "PATCH" :
                controller.patchController(httpExchange);
            case "DELETE" :
                controller.deleteController(httpExchange);
        }

    }


}
