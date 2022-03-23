package com.codesoom.assignment.config;

import com.codesoom.assignment.domain.todo.TodoService;
import com.codesoom.assignment.infrastructure.repository.MemoryRepository;
import com.codesoom.assignment.infrastructure.todo.TodoServiceImpl;

public class AppConfig {

    public final static int PORT = 1238;
    public final static int BACK_LOG = 0;
    public final static String HOST="localhost";


    public TodoService todoService() {
        return new TodoServiceImpl(new MemoryRepository());
    }
}
