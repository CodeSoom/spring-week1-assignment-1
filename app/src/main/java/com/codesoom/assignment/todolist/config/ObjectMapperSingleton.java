package com.codesoom.assignment.todolist.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSingleton {
    private static final ObjectMapper instance = new ObjectMapper();

    private ObjectMapperSingleton() {}

    public static ObjectMapper getInstance() {
        return instance;
    }
}
