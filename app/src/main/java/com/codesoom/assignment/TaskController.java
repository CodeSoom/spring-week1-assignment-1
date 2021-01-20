package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TaskController {

    public void getController(HttpExchange httpExchange){
        String body = getBody(httpExchange);


    }

    public void postController(HttpExchange httpExchange) {
    }

    public void putController(HttpExchange httpExchange) {
    }

    public void patchController(HttpExchange httpExchange) {
    }

    public void deleteController(HttpExchange httpExchange) {
    }


    public String getBody(HttpExchange httpExchange){
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }




}
