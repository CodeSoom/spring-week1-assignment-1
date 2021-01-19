package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class MyHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String content = "Hello! Codesoom!";

        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();


        //header
        httpExchange.sendResponseHeaders(200, content.getBytes().length);

        //body
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
