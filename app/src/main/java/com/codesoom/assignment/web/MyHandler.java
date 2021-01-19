package com.codesoom.assignment.web;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.RequestInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHandler implements HttpHandler {

    private final TaskService taskService;

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

    private boolean isInvalidPath(String path) {
        return !path.startsWith("/tasks");
    }

    private void processRequest(RequestInfo requestInfo, HttpExchange exchange) throws IOException {
        try {
            switch (requestInfo.getMethod()) {
                case "GET":
                    processGet(requestInfo, exchange);
                    break;
                case "POST":
                    processPost(requestInfo, exchange);
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sendResponse("Invalid Parameter", 400, exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse("Internal Server Error", 500, exchange);
        }
    }

    private void processGet(RequestInfo requestInfo, HttpExchange exchange) throws IOException {
        if (requestInfo.getPath().equals("/tasks") || requestInfo.getPath().equals("/tasks/")) {
            String responseJson = JsonUtil.toJson(taskService.getTasks());
            sendResponse(responseJson, 200, exchange);
        } else {
            long id = parseIdFromPath(requestInfo.getPath());
            String responseJson = JsonUtil.toJson(taskService.getTask(id));
            sendResponse(responseJson, 200, exchange);
        }
    }

    private void processPost(RequestInfo requestInfo, HttpExchange exchange) throws IOException {
        Task task = JsonUtil.toTask(requestInfo.getBody());
        Task createdTask = taskService.createNewTask(task.getTitle());
        String responseJson = JsonUtil.toJson(createdTask);
        sendResponse(responseJson, 201, exchange);
    }

    private void processPut(RequestInfo requestInfo) {

    }

    private void processDelete(RequestInfo requestInfo) {

    }

    private long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.substring("/tasks/".length()).replace("/", "");
        return Long.parseLong(idString);
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
