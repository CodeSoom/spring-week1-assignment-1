package com.codesoom.assignment.todolist.domain;

import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Method;
import java.net.URI;

public interface Controller {
    boolean support(URI uri);

    Method getAvailMethod(HttpExchange exchange);
}
