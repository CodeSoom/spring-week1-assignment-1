package com.codesoom.assignment.config;

import com.codesoom.assignment.domain.task.TaskService;
import com.codesoom.assignment.infrastructure.repository.MemoryRepository;
import com.codesoom.assignment.infrastructure.task.TaskServiceImpl;

public class AppConfig {
    public final static int PORT = 8080;
    public final static int BACK_LOG = 0;
    public final static String HOST="127.0.0.1";


    public TaskService taskService() {
        return new TaskServiceImpl(new MemoryRepository());
    }
}
