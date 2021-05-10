package com.codesoom.assignment;

import com.codesoom.assignment.handler.ContextRootHandler;
import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final int PORT_NUMBER = 8000;
    private static final Logger logger = Logger.getGlobal();

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        logger.setLevel(Level.FINE);

        var consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE);
        logger.addHandler(consoleHandler);

        final var address = new InetSocketAddress(PORT_NUMBER);

        logger.log(Level.FINE, new App().getGreeting());
        logger.log(Level.FINE, "Codesoom HTTP server started on : {0}", Long.toString(address.getPort()));

        try {
            var httpServer = HttpServer.create();
            httpServer.bind(address, 0);

            httpServer.createContext("/", new ContextRootHandler());
            httpServer.createContext("/tasks", new TaskHandler());

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
