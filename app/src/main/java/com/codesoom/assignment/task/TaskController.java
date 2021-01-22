package com.codesoom.assignment.task;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import static com.codesoom.assignment.http.HttpStatus.*;

public class TaskController {

    TaskService taskService = new TaskService();

    public void getController(HttpExchange httpExchange) throws IOException {
        String uri = String.valueOf(httpExchange.getRequestURI());

        if (!taskService.isBodyEmpty(httpExchange)) {
            errorController(httpExchange);
            return;
        }

        if (uri.length() <= 7) {
            String body = taskService.getTasks();
            httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
            taskService.processBody(httpExchange, body);
            return;
        }
        int id = taskService.getId(uri);

        if (id <= taskService.tasks.size() && id >= 1) {
            String body = taskService.getTaskById(id);
            httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
            taskService.processBody(httpExchange, body);
            return;
        }
        errorController(httpExchange);

    }

    public void postController(HttpExchange httpExchange) throws IOException {
        String uri = String.valueOf(httpExchange.getRequestURI());

        if (taskService.isBodyEmpty(httpExchange) || uri.length() >= 8) {
            errorController(httpExchange);
            return;
        }

        String body = taskService.createTask(httpExchange);
        httpExchange.sendResponseHeaders(CREATED.getStatus(), body.getBytes().length);
        taskService.processBody(httpExchange, body);
    }

    public void putController(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());

        if (taskService.isBodyEmpty(httpExchange) || uri.length() <= 7) {
            errorController(httpExchange);
            return;
        }

        if (uri.length() >= 8) {
            int id = taskService.getId(uri);
            System.out.println(id);

            if (id <= taskService.tasks.size() && id >= 1) {
                String body = taskService.updateTask(httpExchange);
                httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
                taskService.processBody(httpExchange, body);
                return;
            }
            errorController(httpExchange);

        }
    }

    public void patchController(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());

        if (taskService.isBodyEmpty(httpExchange) || uri.length() <= 7){
            errorController(httpExchange);
            return;
        }

        if (uri.length() >= 8) {
            int id = taskService.getId(uri);
            System.out.println(id);

            if (id <= taskService.tasks.size() && id >= 1){
                String body = taskService.updateTask(httpExchange);
                httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
                taskService.processBody(httpExchange, body);
                return;
            }
            errorController(httpExchange);
        }
    }

    public void deleteController(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());

        if (!taskService.isBodyEmpty(httpExchange) || uri.length() <= 7){
            errorController(httpExchange);
            return;
        }

        if (uri.length() >= 8) {
            int id = taskService.getId(uri);
            System.out.println(id);

            if (id <= taskService.tasks.size() && id >= 1){
                String body = taskService.deleteTask(httpExchange);
                httpExchange.sendResponseHeaders(OK.getStatus(), body.getBytes().length);
                taskService.processBody(httpExchange, body);
                return;
            }
            errorController(httpExchange);
        }
    }


    public void errorController (HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(INTERNAL_SERVER_ERROR.getStatus(), 0);
        taskService.processBody(httpExchange, "");
    }

}