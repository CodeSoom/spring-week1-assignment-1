package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WebHandler implements HttpHandler {
    TaskApplicationService taskApplicationService;

    public WebHandler(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        String requestBody = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining(""));
        HttpRequest request = new HttpRequest(method, path, requestBody);
        List<Mapping> mappingList = prepareMapping();
        Optional<HttpResponse> response = mappingList.stream()
            .filter(
                it -> it.pattern.isMatched(request)
            ).findFirst()
            .map(
                it -> {
                    try {
                        return it.controller.process(request);
                    } catch (Exception e) {
                        return new HttpResponse(500, "Server error");
                    }
                }
            );

        writeHttpResponse(exchange, response.orElse(new HttpResponse(404, "Not Found")));
    }

    List<Mapping> prepareMapping() {
        Controller controller = new Controller(taskApplicationService);
        List<Mapping> mappingList = new ArrayList<>();

        mappingList.add(new Mapping(
                new ExpectRequestPattern("GET", "/tasks/\\d"),
                controller::getTasksWithId
        ));
        mappingList.add(new Mapping(
                new ExpectRequestPattern("GET", "/tasks$"),
                controller::getTasks
        ));
        mappingList.add(new Mapping(
                new ExpectRequestPattern("POST", "/tasks$"),
                controller::postTask
        ));
        mappingList.add(new Mapping(
                new ExpectRequestPattern("PUT", "/tasks/\\d"),
                controller::putTask
        ));
        mappingList.add(new Mapping(
                new ExpectRequestPattern("DELETE", "/tasks/\\d"),
                controller::deleteTask
        ));
        mappingList.add(new Mapping(
                new ExpectRequestPattern("GET", "/"),
                (request -> new HttpResponse(200, "Welcome to Las's service!"))
        ));

        return mappingList;
    }

    private void writeHttpResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        exchange.sendResponseHeaders(response.statusCode, response.content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
