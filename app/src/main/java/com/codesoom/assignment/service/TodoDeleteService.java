package com.codesoom.assignment.service;

import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.repository.TaskRepositoryImpl;
import com.codesoom.assignment.util.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class TodoDeleteService implements TodoService {
    private final TaskRepository taskRepository = TaskRepositoryImpl.getInstance();
    @Override
    public ResponseData processRequest(HttpExchange exchange, Task taskParam, String pathVariable) throws IOException {
        if (isDeleteRequest(pathVariable, taskParam)) {
            return taskRepository.deleteTask(Long.parseLong(pathVariable)) ?
                        new ResponseData(HttpStatus.HTTP_NO_CONTENT, "") :
                            new ResponseData(HttpStatus.HTTP_NOT_FOUND, "");

        } else {
            return new ResponseData(HttpStatus.HTTP_BAD_REQUEST, "");

        }
    }

    private boolean isDeleteRequest(String pathVariable, Task taskParam) {
        return (pathVariable != null &&
                !pathVariable.isBlank()) &&
                taskParam == null;
    }
}
