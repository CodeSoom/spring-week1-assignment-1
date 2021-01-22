package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static com.codesoom.assignment.HttpStatus.NOT_FOUND;
import static com.codesoom.assignment.HttpStatus.OK;

public class BasicHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String uri = String.valueOf(httpExchange.getRequestURI());
        if (uri.equals("/")){
            httpExchange.sendResponseHeaders(OK.getStatus(), 0);
        }else{
            httpExchange.sendResponseHeaders(NOT_FOUND.getStatus(), 0);
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.close();
    }
}