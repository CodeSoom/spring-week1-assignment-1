package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.codesoom.assignment.manager.HttpServerManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try{
            HttpServerManager httpServerManager = new HttpServerManager(new InetSocketAddress(8000), 0);
            httpServerManager.setHandler(TaskHttpHandler.PATH, new TaskHttpHandler());
            httpServerManager.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
