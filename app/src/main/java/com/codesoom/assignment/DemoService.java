package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.parser.Parser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

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

    public Optional<Task> readTaskById(Long param) {
        return demoRepository.readTaskById(param);
    }

    public Task updateTask(Task task, String body) throws JsonProcessingException {
        String title = parser.toTask(body).getTitle();
        return demoRepository.updateTask(task, title);
    }

    public void deleteTask(Task task) {
        demoRepository.deleteTask(task);
    }
}
