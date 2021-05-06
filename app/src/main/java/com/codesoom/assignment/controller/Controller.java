package com.codesoom.assignment.controller;

import com.codesoom.assignment.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public abstract class Controller {
    public abstract boolean handleResource(String path);
    public abstract Response resolve(HttpExchange exchange) throws IOException;
}
