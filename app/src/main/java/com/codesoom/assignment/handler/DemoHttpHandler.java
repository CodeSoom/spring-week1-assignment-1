package com.codesoom.assignment.handler;

import com.codesoom.assignment.enumCode.HttpStatusCode;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.service.Service;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;


public class DemoHttpHandler implements HttpHandler {

    private final Service service = new Service();

    private final String defaultPath = "/tasks";
    private final String pathvariable = "/tasks/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String body = service.getBody(exchange);
        String path = uri.getPath();
        String content = "";

        System.out.println("method::" + requestMethod + "\npath:: " + path);
        System.out.println("===============================");

        if (requestMethod.equals("GET") && path.equals(defaultPath)) {
            if (service.tasks.isEmpty()) {
                service.RequestSend(HttpStatusCode.OK.getStatus(), exchange, "empty array");
            }
            service.RequestSend(HttpStatusCode.OK.getStatus(), exchange, service.tasksToJSON(service.tasks));
        }
        else if (requestMethod.equals("GET") && path.startsWith(pathvariable)) {
            if (path.equals("/tasks/")) {
                service.tasks.forEach(c -> {
                    variableInspection(exchange, path, c);
                });
                service.RequestSend(HttpStatusCode.NOT_FOUND.getStatus(), exchange, "Please enter your ID");
            } else {
                service.RequestSend(HttpStatusCode.OK.getStatus(), exchange, service.detailContent(path, content));
            }
        }
        else if (requestMethod.equals("POST") && path.equals(defaultPath)) {
            service.createNewTask(body);
            service.RequestSend(HttpStatusCode.CREATED.getStatus(), exchange, "Create a new task");
        }
        else if (requestMethod.equals("PATCH") || requestMethod.equals("PUT") && path.startsWith(pathvariable)) {
            service.updateTask(path, body);
            service.RequestSend(HttpStatusCode.OK.getStatus(), exchange, "Update task");
        }
        else if (requestMethod.equals("DELETE") && path.startsWith(pathvariable)) {
            service.tasks.remove(service.getRequestId(path).intValue() - 1);
            service.RequestSend(HttpStatusCode.OK.getStatus(), exchange, "Delete Task");
        }
        else {
            service.RequestSend(HttpStatusCode.NOT_FOUND.getStatus(), exchange, "check url");
        }
    }

    private void variableInspection(HttpExchange exchange, String path, Task c) {
        if (c.getId().equals(service.getRequestId(path))) {
            try {
                service.RequestSend(HttpStatusCode.NOT_FOUND.getStatus(), exchange, "There is no ID entered for storage.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
