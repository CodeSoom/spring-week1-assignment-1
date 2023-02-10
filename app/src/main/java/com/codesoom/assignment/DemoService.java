package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.parser.Parser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class DemoService {

    private DemoRepository demoRepository = new DemoRepository();
    Parser parser = new Parser();

    public List<Task> readAllTasks() {
        return demoRepository.readAllTasks();
    }

    public Task createTask(String body) throws JsonProcessingException {
        String title = parser.toTask(body).getTitle();
        return demoRepository.createTask(title);
    }

    public Task readTaskById(Long param) {
        return demoRepository.readTaskById(param);
    }
}
