package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public interface StrategyProcess {
    void process(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException;

    default long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace("/tasks/", "").replace("/", "");
        return Long.parseLong(idString);
    }
}
