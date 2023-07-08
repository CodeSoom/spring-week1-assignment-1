package com.codesoom.assignment.handler;


import com.codesoom.assignment.handler.impl.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.*;


public class TaskHttpHandler implements HttpHandler {
    Map<String, TaskHandler> handlerMap;

    @Override
    public void handle(HttpExchange exchange) throws RuntimeException {
        initHandler(exchange);

        handlerMap.entrySet().stream()
                .filter(entry -> entry.getValue().isRequest())
                .findFirst()
                .ifPresent(entry -> {
                    try {
                        entry.getValue().handle();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void initHandler(HttpExchange exchange) {
        handlerMap = new HashMap<>();
        handlerMap.put("CREATE", new CreateTaskHandler(exchange));
        handlerMap.put("GET_LIST", new GetListTaskHandler(exchange));
        handlerMap.put("GET", new GetTaskHandler(exchange));
        handlerMap.put("UPDATE", new UpdateTaskHandler(exchange));
        handlerMap.put("DELETE", new DeleteTaskHandler(exchange));
    }
}
