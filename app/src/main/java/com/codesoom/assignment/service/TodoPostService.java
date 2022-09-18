package com.codesoom.assignment.service;

import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.repository.TaskRepositoryImpl;
import com.codesoom.assignment.util.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class TodoPostService implements TodoService {
    private final TaskRepository taskRepository = TaskRepositoryImpl.getInstance();
    @Override
    public ResponseData processRequest(HttpExchange exchange, Task taskParam, String pathVariable) throws IOException {
        return isSaveRequest(pathVariable, taskParam) ?
                new ResponseData(HttpStatus.HTTP_CREATED, convertToJSON(taskRepository.save(taskParam)))
                : new ResponseData(HttpStatus.HTTP_BAD_REQUEST, "");
    }

    private boolean isSaveRequest(String pathVariable, Task taskParam) {
        return pathVariable == null && taskParam != null;
    }
}
