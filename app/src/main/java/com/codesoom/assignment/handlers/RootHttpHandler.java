package com.codesoom.assignment.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static com.codesoom.assignment.App.STATUS_NOT_FOUND;
import static com.codesoom.assignment.App.STATUS_OK;

public class RootHttpHandler implements HttpHandler {

    public RootHttpHandler() {
        System.out.println("RootHttpHandler Created");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String requestURIPath = requestURI.getPath();
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestURIPath);
        System.out.println(requestMethod);
        System.out.println("Root");
        String result = "Welcome!";
        if(requestURIPath.equals("/") && requestMethod.equals("GET")){
            exchange.sendResponseHeaders(STATUS_OK, result.getBytes().length);
        }else{
            result = "404 Not Found!";
            exchange.sendResponseHeaders(STATUS_NOT_FOUND, result.getBytes().length);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(result.getBytes());
        responseBody.flush();
        exchange.close();
    }
}
