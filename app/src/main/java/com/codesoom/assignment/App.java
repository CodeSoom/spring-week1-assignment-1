package com.codesoom.assignment;

import com.codesoom.assignment.application.user.UserService;
import com.codesoom.assignment.web.*;
import com.codesoom.assignment.application.task.TaskService;
import com.codesoom.assignment.web.task.TaskCollectionHttpRequestContext;
import com.codesoom.assignment.web.task.TaskItemHttpRequestContext;
import com.codesoom.assignment.web.user.UserCollectionHttpRequestContext;

import java.io.IOException;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        MyHttpServer httpServer = new MyHttpServer(PORT);
        TaskService taskService = new TaskService();
        UserService userService = new UserService();

        HttpRequestContextBase taskCollectionRequestContext = new TaskCollectionHttpRequestContext("/tasks", taskService);
        HttpRequestContextBase taskItemRequestContext = new TaskItemHttpRequestContext("/tasks/", taskService);
        HttpRequestContextBase userCollectionRequestContext = new UserCollectionHttpRequestContext("/users", userService);
        HttpRequestContextBase userItemRequestContext = new UserCollectionHttpRequestContext("/users/", userService);

        MyHandler handler = new MyHandler();
        handler.addRequestContext(taskCollectionRequestContext);
        handler.addRequestContext(taskItemRequestContext);
        handler.addRequestContext(userCollectionRequestContext);
        handler.addRequestContext(userItemRequestContext);

        httpServer.addHandler("/", handler);
        httpServer.start();
    }
}
