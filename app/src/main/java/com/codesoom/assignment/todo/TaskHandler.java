package com.codesoom.assignment.todo;

import com.codesoom.assignment.todo.controllers.TaskController;
import com.codesoom.assignment.todo.models.RequestValidation;
import com.codesoom.assignment.todo.services.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public class TaskHandler {


    TaskService taskService = new TaskService();
    TaskController taskController = new TaskController(taskService);

    public void handler(String sRequestMethod, String sRequestPath, String sRequestBody, String sRequestQuery, HttpExchange exchange) {

        try {
            String responseContent = null;

            RequestValidation validation = new RequestValidation().validationCheck(sRequestMethod,sRequestPath,sRequestBody,sRequestQuery);

            System.out.println(validation.getIsValid());
            System.out.println(validation.getResultMsg());

            switch (sRequestMethod) {
                case "GET" -> responseContent = taskController.getTasks(sRequestPath);
                case "POST" -> responseContent = taskController.addTask(sRequestBody);
                case "PUT", "PATCH" -> responseContent = taskController.modTask(sRequestPath, sRequestBody);
                case "DELETE" -> responseContent = taskController.delTask(sRequestPath);
                default -> {
                }
            }

            exchange.sendResponseHeaders(200, Objects.requireNonNull(responseContent).getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseContent.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch(NumberFormatException e){
            e.printStackTrace();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
