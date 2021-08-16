package com.codesoom.assignment;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TaskManager taskManager = new TaskManager();

    public String create(String body) throws IOException {
        String title = convertJsonToTask(body).getTitle();
        taskManager.create(title);
        return convertTaskToJson(taskManager.getLast());
    }

    public String getAll() throws IOException {
        return convertTasksToJson(taskManager.getAll());
    }

    public Response getOne(Path path) throws NoSuchElementException, IOException {
        Long id = path.getIdOf("tasks");
        Task resultTask = taskManager.getOne(id);
        return convertTaskToJson(resultTask);
    }

    public String remove(Path path) throws NoSuchElementException {
        Long id = path.getIdOf("tasks");
        taskManager.remove(id);
        return "Success Remove Task : " + id;
    }

    public String update(Path path, String body) throws NoSuchElementException, IOException {
        Long id = path.getIdOf("tasks");
        String title = convertJsonToTask(body).getTitle();
        taskManager.update(id, title);
        return convertTaskToJson(taskManager.getOne(id));
    }

    private Task convertJsonToTask(String content) throws JsonProcessingException {
        return this.objectMapper.readValue(content, Task.class);
    }

    private String convertTasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String convertTaskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
