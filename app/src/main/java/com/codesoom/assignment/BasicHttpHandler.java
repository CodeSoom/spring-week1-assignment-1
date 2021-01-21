package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static com.codesoom.assignment.HttpStatus.NOT_FOUND;

public class BasicHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());
        if (uri.equals("/")){
            httpExchange.sendResponseHeaders(NOT_FOUND.getStatus(), 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.flush();
            outputStream.close();
        }else{
            httpExchange.sendResponseHeaders(NOT_FOUND.getStatus(), 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.flush();
            outputStream.close();
        }
    }
}
