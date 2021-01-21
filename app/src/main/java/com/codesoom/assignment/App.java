package com.codesoom.assignment;

import com.codesoom.assignment.web.*;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequestMethod;
import com.codesoom.assignment.web.service.*;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        MyHttpServer httpServer = new MyHttpServer(PORT);
        TaskService taskService = new TaskService();

        Map<HttpRequestMethod, StrategyProcess> strategyProcessMap = new HashMap<>();
        strategyProcessMap.put(HttpRequestMethod.GET, new GetStrategy());
        strategyProcessMap.put(HttpRequestMethod.POST, new PostStrategy());
        strategyProcessMap.put(HttpRequestMethod.PUT, new UpdateStrategy());
        strategyProcessMap.put(HttpRequestMethod.PATCH, new UpdateStrategy());
        strategyProcessMap.put(HttpRequestMethod.DELETE, new DeleteStrategy());

        HttpRequestContext requestContext = new HttpRequestContext(strategyProcessMap);
        HttpHandler handler = new MyHandler(taskService, requestContext);

        httpServer.addHandler("/", handler);
        httpServer.start();
    }

}
