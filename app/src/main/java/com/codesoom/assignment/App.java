package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.codesoom.assignment.manager.HttpServerManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int PORT = 8000;
    private static final int BAKC_LOG = 0;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try{
            HttpServerManager httpServerManager = new HttpServerManager(new InetSocketAddress(PORT), BAKC_LOG);
            httpServerManager.setHandler(TaskHttpHandler.PATH, new TaskHttpHandler());
            httpServerManager.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
