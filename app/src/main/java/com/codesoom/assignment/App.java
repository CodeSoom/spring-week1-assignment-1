package com.codesoom.assignment;

import com.codesoom.assignment.config.AppConfig;
import com.codesoom.assignment.server.TodoServer;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.runTodoServer();
    }

    public void runTodoServer() {
        try {
            AppConfig appConfig = new AppConfig(1238, 0, "localhost");
            TodoServer todoServer = new TodoServer(appConfig);
            todoServer.start();
        } catch (IOException e) {
            System.out.printf("{}", e.getMessage());
        }

    }
}
