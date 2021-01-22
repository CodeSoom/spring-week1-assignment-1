package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;


public class TaskHttpHandler implements HttpHandler {

    private TaskController controller = new TaskController();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        switch (method){
            case "GET" :
                controller.getController(httpExchange);
                break;
            case "POST" :
                controller.postController(httpExchange);
                break;
            case "PUT" :
                controller.putController(httpExchange);
                break;
            case "PATCH" :
                controller.patchController(httpExchange);
                break;
            case "DELETE" :
                controller.deleteController(httpExchange);
                break;
            default:
        }
    }
}
