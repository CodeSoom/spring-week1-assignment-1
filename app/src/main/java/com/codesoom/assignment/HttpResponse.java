package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.Optional;

public class HttpResponse {
    private String content;
    private int httpStatusCode;
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
        if(!task.isEmpty()){
            content =  jsonConverter.oneTaskToJSON(task.get());
            httpStatusCode = HttpStatus.OK.getCode();
        }
    }

    public void createTask(String body) throws IOException {
        Task task = taskFactory.createTask(body);
        content = jsonConverter.oneTaskToJSON(task);
        httpStatusCode = HttpStatus.CREATED.getCode();
    }

    public void updateTask(String id, String body) throws IOException {
        Optional<Task> task = taskFactory.findId(id);
        if(!task.isEmpty()){
            Task updateTask = taskFactory.updateTask(task.get(), body);
            content =  jsonConverter.oneTaskToJSON(updateTask);
            httpStatusCode = HttpStatus.OK.getCode();
        }
    }

    public void deleteTask(String id) {
        Optional<Task> task = taskFactory.findId(id);
        if(!task.isEmpty()){
            taskFactory.deleteTask(id);
            httpStatusCode = HttpStatus.NO_CONTENT.getCode();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode){
        this.httpStatusCode = httpStatusCode;
    }


}
