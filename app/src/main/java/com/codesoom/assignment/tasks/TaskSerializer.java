package com.codesoom.assignment.tasks;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class TaskSerializer {
    private ObjectMapper objectMapper = new ObjectMapper();

    public String tasksToJson(ArrayList<Task> tasks) {
        String jsonString = "[]";
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("e: " + e);
        }
        if(jsonString.equals("[ ]")){
            jsonString = "[]";
        }
        return jsonString;
    }

    public String taskToJson(Task task) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("e: " + e);
        }
        return jsonString;
    }

    public Task jsonToTask(String json) {
        Task task = null;
        try {
            task = objectMapper.readValue(json, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("e: " + e);
        }
        return task;
    }
}
