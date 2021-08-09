package com.codesoom.assignment.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;

public class HttpConnector {
    private static final int DEFAULT_PORT = 8000;
    public static final String ROOT_CONTEXT = "/";
    private final FrontController frontController;
    private final ObjectMapper om;
    HttpServer server = null;

    public HttpConnector(FrontController frontController) {
        this.frontController = frontController;
        om = new ObjectMapper();
    }


    public void start(){
        start(DEFAULT_PORT);
    }

    public void start(int port){
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
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

        final byte[] bytes = om.writeValueAsBytes(response.getBody());
        exchange.sendResponseHeaders(response.getStatusCode(), bytes.length);
        OutputStream os = exchange.getResponseBody();

        os.write(bytes);
        os.close();
    }
}
