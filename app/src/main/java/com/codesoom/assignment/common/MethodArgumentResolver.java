package com.codesoom.assignment.common;

import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface MethodArgumentResolver {

    boolean supportParameter(Parameter parameter);

    Object resolveArgument(HttpExchange exchange, Parameter parameter, Method method);

}
