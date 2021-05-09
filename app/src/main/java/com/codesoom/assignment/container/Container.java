package com.codesoom.assignment.container;

import com.codesoom.assignment.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public abstract class Container {

    public abstract Response resolve(HttpExchange exchange) throws IOException;
}
