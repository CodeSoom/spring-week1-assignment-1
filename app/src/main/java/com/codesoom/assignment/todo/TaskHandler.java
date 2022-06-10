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
                return new Response(Response.BAD_REQUEST, validation.getResultMsg());
            }

            switch (sRequestMethod) {
                case "GET" -> {
                    return taskController.getTasks(sRequestPath);
                }
                case "POST" -> {
                    return taskController.addTask(sRequestBody);
                }
                case "PUT", "PATCH" -> {
                    return taskController.modTask(sRequestPath, sRequestBody);
                }
                case "DELETE" -> {
                    return taskController.delTask(sRequestPath);
                }
                default -> {
                    return new Response(Response.BAD_REQUEST, "잘못된 요청입니다");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
