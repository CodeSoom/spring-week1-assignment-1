package com.codesoom.assignment.todolist.config;

import com.codesoom.assignment.http.FrontController;
import com.codesoom.assignment.http.HttpConnector;
import com.codesoom.assignment.todolist.application.TodoService;
import com.codesoom.assignment.todolist.config.resolver.HttpExchangeArgumentResolver;
import com.codesoom.assignment.todolist.config.resolver.PathVariableArgumentResolver;
import com.codesoom.assignment.todolist.config.resolver.TodoEntityArgumentResolver;
import com.codesoom.assignment.todolist.domain.Controller;
import com.codesoom.assignment.todolist.domain.TodoRepository;
import com.codesoom.assignment.todolist.ui.TodoController;

import java.util.Arrays;

public class WebConfig {
    private final HttpConnector httpConnector;

    public WebConfig() {
        httpConnector = httpConnector();
    }

    private HttpConnector httpConnector() {
        return new HttpConnector(frontController());
    }

    private FrontController frontController() {
        FrontController frontController = FrontController.getInstance();
        frontController.addController(todoController());
        frontController.addArgumentResolver(Arrays.asList(
                todoEntityArgumentResolver(),
                pathVariableArgumentResolver(),
                httpExchangeArgumentResolver()
        ));
        return frontController;
    }

    private Controller todoController() {
        return new TodoController(todoService());
    }

    private TodoService todoService() {
        return new TodoService(todoRepository());
    }

    private TodoRepository todoRepository() {
        return TodoRepository.getInstance();
    }

    private HttpExchangeArgumentResolver httpExchangeArgumentResolver() {
        return new HttpExchangeArgumentResolver();
    }

    private PathVariableArgumentResolver pathVariableArgumentResolver() {
        return new PathVariableArgumentResolver();
    }

    private TodoEntityArgumentResolver todoEntityArgumentResolver() {
        return new TodoEntityArgumentResolver();
    }

    public void start() {
        httpConnector.start();
    }
}
