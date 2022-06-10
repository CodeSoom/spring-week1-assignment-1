package com.codesoom.assignment.todo;

import com.codesoom.assignment.todo.controllers.TaskController;
import com.codesoom.assignment.todo.models.RequestValidation;
import com.codesoom.assignment.todo.models.Response;
import com.codesoom.assignment.todo.services.TaskService;
import com.sun.net.httpserver.HttpExchange;


public class TaskHandler {


    TaskService taskService = new TaskService();
    TaskController taskController = new TaskController(taskService);

    public Response handler(String sRequestMethod, String sRequestPath, String sRequestBody, String sRequestQuery, HttpExchange exchange) {

        try {
            String responseContent = null;

            RequestValidation validation = new RequestValidation().validationCheck(sRequestMethod,sRequestPath,sRequestBody,sRequestQuery);

            System.out.println(validation.getIsValid());
            System.out.println(validation.getResultMsg());

            if(!validation.getIsValid()){
                return new Response(400, validation.getResultMsg());
            }

            switch (sRequestMethod) {
                case "GET" -> responseContent = taskController.getTasks(sRequestPath);
                case "POST" -> responseContent = taskController.addTask(sRequestBody);
                case "PUT", "PATCH" -> responseContent = taskController.modTask(sRequestPath, sRequestBody);
                case "DELETE" -> responseContent = taskController.delTask(sRequestPath);
                default -> {
                }
            }

            return new Response(200, responseContent);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
