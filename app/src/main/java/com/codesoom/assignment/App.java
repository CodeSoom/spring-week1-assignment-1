package com.codesoom.assignment;

import com.codesoom.assignment.todolist.config.WebConfig;

public class App {

    public static void main(String[] args) {
        WebConfig config = webConfig();
        config.start();
    }

    private static WebConfig webConfig() {
        return new WebConfig();
    }
}
