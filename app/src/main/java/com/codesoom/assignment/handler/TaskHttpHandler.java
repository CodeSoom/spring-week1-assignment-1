package com.codesoom.assignment.handler;


import com.codesoom.assignment.handler.impl.*;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.model.RequestBody;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.vo.HttpMethod;
import com.codesoom.assignment.vo.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class TaskHttpHandler implements HttpHandler {
    CreateTaskHandler createTaskHandler;
    GetTaskHandler getTaskHandler;
    GetListTaskHandler getListTaskHandler;
    UpdateTaskHandler updateTaskHandler;
    DeleteTaskHandler deleteTaskHandler;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        createTaskHandler = new CreateTaskHandler(exchange);
        getListTaskHandler = new GetListTaskHandler(exchange);
        getTaskHandler = new GetTaskHandler(exchange);
        updateTaskHandler = new UpdateTaskHandler(exchange);
        deleteTaskHandler = new DeleteTaskHandler(exchange);

        if (createTaskHandler.isRequest()) {
            createTaskHandler.handle();
        } else if (getListTaskHandler.isRequest()) {
            getListTaskHandler.handle();
        } else if (getTaskHandler.isRequest()) {
            getTaskHandler.handle();
        } else if (updateTaskHandler.isRequest()) {
            updateTaskHandler.handle();
        } else if (deleteTaskHandler.isRequest()) {
            deleteTaskHandler.handle();
        }
    }
}
