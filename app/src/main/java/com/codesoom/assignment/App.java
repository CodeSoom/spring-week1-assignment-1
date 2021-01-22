package com.codesoom.assignment;

import com.codesoom.assignment.service.UserService;
import com.codesoom.assignment.web.*;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        MyHttpServer httpServer = new MyHttpServer(PORT);
        TaskService taskService = new TaskService();
        UserService userService = new UserService();

        TaskHttpRequestContext taskRequestContext = new TaskHttpRequestContext(taskService);
        UserHttpRequestContext userRequestContext = new UserHttpRequestContext(userService);

        MyHandler handler = new MyHandler();
        handler.addRequestContext(TaskHttpRequestContext.BASE_PATH, taskRequestContext);
        handler.addRequestContext(UserHttpRequestContext.BASE_PATH, userRequestContext);

        httpServer.addHandler("/", handler);
        httpServer.start();
    }
}
