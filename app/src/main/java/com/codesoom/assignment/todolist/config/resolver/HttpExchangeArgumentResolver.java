package com.codesoom.assignment.todolist.config.resolver;

import com.codesoom.assignment.common.MethodArgumentResolver;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HttpExchangeArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportParameter(Parameter parameter) {
        return parameter.getParameterizedType().equals(HttpExchange.class);
    }

    @Override
    public Object resolveArgument(HttpExchange exchange, Parameter parameter, Method method) {
        return exchange;
    }
}
