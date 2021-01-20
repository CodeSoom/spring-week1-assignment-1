package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {


    private TaskController controller = new TaskController();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();

        // process request body data
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (method.equals("GET") ) controller.getController(httpExchange);

        if (method.equals("POST")) controller.postController(httpExchange);

        if (method.equals("PUT") ) controller.putController(httpExchange);

        if (method.equals("PATCH") ) controller.patchController(httpExchange);

        if (method.equals("DELETE")) controller.deleteController(httpExchange);

        //set request body
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.flush();
        outputStream.close();
    }


}
