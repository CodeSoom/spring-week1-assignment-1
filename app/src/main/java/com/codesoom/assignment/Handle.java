package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface Handle {
    public void check(String[] pathSplit, String body, HttpExchange exchange) throws IOException;
}
