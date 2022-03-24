package com.codesoom.assignment.config;

import com.codesoom.assignment.domain.todo.TodoService;
import com.codesoom.assignment.infrastructure.repository.MemoryRepository;
import com.codesoom.assignment.infrastructure.todo.TodoServiceImpl;

public class AppConfig {

    private final int port;
    private final int backLog;
    private final String host;
    public AppConfig(int port, int backLog, String host) {
        this.port = port;
        this.backLog = backLog;
        this.host = host;
    }

    public int getBackLog() {
        return backLog;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public TodoService todoService() {
        return new TodoServiceImpl(new MemoryRepository());
    }
}
