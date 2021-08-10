package com.codesoom.assignment.todolist.config.resolver;

import com.codesoom.assignment.common.MethodArgumentResolver;
import com.codesoom.assignment.todolist.config.ObjectMapperSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.logging.Logger;

public class TodoEntityArgumentResolver implements MethodArgumentResolver {
    Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String ENTITY_PACKAGE_NAME = "com.codesoom.assignment.todolist.domain";

    @Override
    public boolean supportParameter(Parameter parameter) {
        final String packageName = parameter.getType().getPackageName();
        return packageName.equals(ENTITY_PACKAGE_NAME);
    }

    @Override
    public Object resolveArgument(HttpExchange exchange, Parameter parameter, Method method) {
        final ObjectMapper om = ObjectMapperSingleton.getInstance();
        try{
            return om.readValue(exchange.getRequestBody(), parameter.getType());
        }catch(IOException ie){
            logger.severe(ie.getMessage());
            return null;
        }
    }
}
