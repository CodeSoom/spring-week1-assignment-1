package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    private String content = "";
    private int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.getCode();
    private TaskFactory taskFactory;
    private JsonConverter jsonConverter = new JsonConverter();

    public HttpResponse(TaskFactory taskFactory){
        this.taskFactory = taskFactory;
    }

    public void getAllTasks() throws IOException {
        content = jsonConverter.tasksToJSON(taskFactory.getTasks());
        httpStatusCode = HttpStatus.OK.getCode();
    }

    public void getOneTask(String id) throws IOException {
        Optional<Task> task = taskFactory.findId(id);
        httpStatusCode = HttpStatus.NOT_FOUND.getCode();
        if(!task.isEmpty()){
            content =  jsonConverter.oneTaskToJSON(task.get());
            httpStatusCode = HttpStatus.OK.getCode();
        }
    }

    public void createTask(String body) throws IOException {
        taskFactory.createTask(body);
        List<Map<String, Task>> tasks = taskFactory.getTasks();
        content = jsonConverter.tasksToJSON(tasks);
        httpStatusCode = HttpStatus.CREATED.getCode();
    }

    public void updateTask(String id, String body) throws IOException {
        Optional<Task> task = taskFactory.findId(id);
        httpStatusCode = HttpStatus.NOT_FOUND.getCode();
        if(!task.isEmpty()){
            Task updateTask = taskFactory.updateTask(task.get(), body);
            content =  jsonConverter.oneTaskToJSON(updateTask);
            httpStatusCode = HttpStatus.OK.getCode();
        }
    }

    public void deleteTask(String id) {
        Optional<Task> task = taskFactory.findId(id);
        httpStatusCode = HttpStatus.NOT_FOUND.getCode();
        if(!task.isEmpty()){
            taskFactory.deleteTask(id);
            httpStatusCode = HttpStatus.NO_CONTENT.getCode();
        }
    }

    public String getContent() {
        return content;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
