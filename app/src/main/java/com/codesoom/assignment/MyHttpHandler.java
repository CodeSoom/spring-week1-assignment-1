package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

import static com.codesoom.assignment.Status.*;

public class MyHttpHandler implements HttpHandler {

    private final static String PATH = "/tasks";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String content ="";
        int status = 200;

        //path setting
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String method = httpExchange.getRequestMethod();

        // process request body data
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // method setting
        if (method.equals("GET") && path.equals(PATH)){
            content = "[\"id\" : \"1\", \"title\" : \"one day one pr\"]";
            httpExchange.sendResponseHeaders(OK.getStatus(), content.getBytes().length);
        }
        else if (method.equals("POST") && path.equals(PATH)){
            System.out.println(body);
            httpExchange.sendResponseHeaders(CREATED.getStatus(), content.getBytes().length);
        }
        else if (method.equals("PUT") && path.equals(PATH)){

        }
        else if (method.equals("PATCH") && path.equals(PATH)){

        }
        else if (method.equals("DELETE") && path.equals(PATH)){

        }
        else{
            httpExchange.sendResponseHeaders(NOT_FOUND.getStatus(), content.getBytes().length);
        }

        //set request body
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
