package com.codesoom.assignment.todo;

import com.codesoom.assignment.todo.controllers.TaskController;
import com.codesoom.assignment.todo.models.Request;
import com.codesoom.assignment.todo.models.RequestValidation;
import com.codesoom.assignment.todo.models.Response;
import com.codesoom.assignment.todo.services.TaskService;


public class TaskHandler {


    TaskService taskService = new TaskService();
    TaskController taskController = new TaskController(taskService);

    public Response handler(Request request) {

        String requestMethod = request.getRequestMethod();
        String requestPath = request.getRequestPath();
        String requestBody = request.getRequestBody();

        RequestValidation validation = new RequestValidation().validationCheck(request);

        if(!validation.getIsValid()){
            return new Response(Response.BAD_REQUEST, validation.getResultMsg());
        }

        switch (requestMethod) {
            case "GET" -> {
                return taskController.getTasks(requestPath);
            }
            case "POST" -> {
                return taskController.addTask(requestBody);
            }
            case "PUT", "PATCH" -> {
                return taskController.modTask(requestPath, requestBody);
            }
            case "DELETE" -> {
                return taskController.delTask(requestPath);
            }
            default -> {
                return new Response(Response.BAD_REQUEST, "잘못된 요청입니다");
            }
        }
    }


}
