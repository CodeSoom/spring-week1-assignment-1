package com.codesoom.assignment.web;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.RequestInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHandler implements HttpHandler {

    private TaskService taskService;

    public MyHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestInfo requestInfo = new RequestInfo(exchange);
        printRequestInfo(requestInfo);

        if (isInvalidPath(requestInfo.getPath())) {
            sendResponse("Not Found", 404, exchange);
            return;
        }

        processRequest(requestInfo, exchange);
    }

    private void processRequest(RequestInfo requestInfo, HttpExchange exchange) throws IOException {
        try {
            switch (requestInfo.getMethod()) {
                case "GET":
                    if (requestInfo.getPath().equals("/tasks")) {
                        String responseJson = JsonUtil.toJson(taskService.getTasks());
                        sendResponse(responseJson, 200, exchange);
                    } else {
                        long id = Long.parseLong(requestInfo.getPath().substring("/tasks/".length()));
                        String responseJson = JsonUtil.toJson(taskService.getTask(id));
                        sendResponse(responseJson, 200, exchange);
                    }
                    break;
                case "POST":
                    Task task = JsonUtil.toTask(requestInfo.getBody());
                    Task createdTask = taskService.createNewTask(task.getTitle());
                    String responseJson = JsonUtil.toJson(createdTask);
                    sendResponse(responseJson, 201, exchange);
                    break;
                case "PUT":
                case "PATH":
                    break;
                case "DELETE":
                    break;
                default:
                    sendResponse("Method Not Allowed", 405, exchange);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse("Internal Server Error", 500, exchange);
        }
    }

    private void processGet(RequestInfo requestInfo) {

    }

    private void processPost(RequestInfo requestInfo) {

    }

    private void processPut(RequestInfo requestInfo) {

    }

    private void processDelete(RequestInfo requestInfo) {

    }

    private boolean isInvalidPath(String path) {
        return path.startsWith("/tasks");
    }

    private void sendResponse(String content, int responseCode, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(responseCode, content.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }

    private void printRequestInfo(RequestInfo requestInfo) {
        System.out.println("Received new request - " + requestInfo.toString());
    }
}
