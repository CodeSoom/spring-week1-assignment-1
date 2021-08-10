package com.codesoom.assignment.http;

import com.codesoom.assignment.todolist.config.ObjectMapperSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Logger;

public class HttpConnector {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private static final int DEFAULT_PORT = 8000;
    public static final String ROOT_CONTEXT = "/";
    public static final int DEFAULT_BACKLOG = 0;

    private final FrontController frontController;
    HttpServer server = null;

    public HttpConnector(FrontController frontController) {
        this.frontController = frontController;
    }


    public void start() {
        start(DEFAULT_PORT);
    }

    public void start(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), DEFAULT_BACKLOG);
            HttpContext context = server.createContext(ROOT_CONTEXT);
            context.setHandler(this::handleRequest);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        final URI requestURI = exchange.getRequestURI();
        Response response;

        if (frontController.support(requestURI)) {
            response = frontController.execute(exchange);
        } else {
            response = Response.from(HttpURLConnection.HTTP_NOT_FOUND);
        }
        try (OutputStream os = exchange.getResponseBody()) {
            final ObjectMapper om = ObjectMapperSingleton.getInstance();
            final byte[] bytes = om.writeValueAsBytes(response.getBody());
            exchange.sendResponseHeaders(response.getStatusCode(), bytes.length);
            os.write(bytes);
        } catch (IOException ie) {
            logger.severe(ie.getMessage());
        }
    }
}
