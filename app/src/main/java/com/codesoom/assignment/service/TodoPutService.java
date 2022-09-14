package com.codesoom.assignment.service;

import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.repository.TaskRepositoryImpl;
import com.codesoom.assignment.util.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class TodoPutService implements TodoService {
    private final TaskRepository taskRepository = TaskRepositoryImpl.getInstance();
    @Override
    public ResponseData processRequest(HttpExchange exchange, Task taskParam, String pathVariable) throws IOException {
        if (isUpdateRequest(pathVariable, taskParam)) {
            taskParam.setId(Long.parseLong(pathVariable));

            return taskRepository.findById(Long.parseLong(pathVariable)) != null ?
                    new ResponseData(HttpStatus.HTTP_OK, convertToJSON(taskRepository.updateTask(taskParam))) :
                    new ResponseData(HttpStatus.HTTP_BAD_REQUEST, "");

        } else {
            return new ResponseData(HttpStatus.HTTP_OK, "");

        }
    }

    private boolean isUpdateRequest(String pathVariable, Task taskParam) {
        return (pathVariable != null &&
                    !pathVariable.isBlank()) &&
                            taskParam != null;
    }
}
