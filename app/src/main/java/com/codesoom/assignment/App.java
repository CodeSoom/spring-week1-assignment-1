package com.codesoom.assignment;

import com.codesoom.assignment.web.WebServer;

import java.io.IOException;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try {
            WebServer server = new WebServer(8000);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
