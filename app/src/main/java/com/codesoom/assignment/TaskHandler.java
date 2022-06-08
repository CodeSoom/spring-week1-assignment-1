package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.HashMap;


public class TaskHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HashMap<Long, Task> taskMap = new HashMap<Long, Task>();

    private static long index = 0L;


    public void handler(String method, String path, String body, HttpExchange exchange) {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)
        try {
            String[] arrPath = path.split("/");

            // Specify a value if task id exists in the path, and initializes to 0 if not.
            long lTaskId = 0L;
            lTaskId = arrPath.length == 3 ? Long.parseLong(arrPath[2]) : 0;

            Task task;

            String responseContent = null;

            // Method
            switch (method){
                case "GET" :
                    responseContent = getTasks(lTaskId);
                    break;
                case "POST" :
                    index++;
                    task = toTask(body);
                    task.setId(index);
                    responseContent = addTask(task);
                    break;
                case "PUT", "PATCH" :
                    task = toTask(body);
                    responseContent = modTask(lTaskId, task);
                    break;
                case "DELETE" :
                    responseContent = delTask(lTaskId);
                    break;
                default:
                    break;
            }

            exchange.sendResponseHeaders(200, responseContent.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseContent.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch(NumberFormatException e){
            e.printStackTrace();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private String delTask(Long taskId) throws Exception {
        if(!taskMap.containsKey(taskId)){
            throw new Exception("This key is not exist");
        } else{
            taskMap.remove(taskId);
            return tasksToJson(taskMap);
        }
    }
    private String modTask(Long taskId, Task task) throws Exception {
        if(!taskMap.containsKey(taskId)){
            throw new Exception("This key is not exist");
        }else{
            taskMap.get(taskId).setTitle(task.getTitle());
            return tasksToJson(taskMap.get(taskId));
        }
    }
    private String addTask(Task task) throws Exception {
        Long taskId = task.getId();
        System.out.println("taskId : " + taskId);
        if(taskMap.containsKey(taskId)){
            throw new Exception("This Key is already exist");
        }else{
            taskMap.put(taskId, task);
            return tasksToJson(task);
        }
    }
    private String getTasks(long lTaskId) throws IOException {
        if( lTaskId == 0L){
            return tasksToJson(taskMap);
        }else{
            return taskMap.get(lTaskId).toString();
        }
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


    private String tasksToJson(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJson(HashMap<Long, Task> taskMap) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, taskMap.values());

        return outputStream.toString();
    }
}
