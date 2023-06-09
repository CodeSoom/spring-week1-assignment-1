package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {


    private Map<Long,Task> taskMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private int statusCode;

    private String content;

    private Long taskId = 1L;


    @Override
    public void handle(HttpExchange exchange) throws IOException {


            //read request
            String method = exchange.getRequestMethod();
            URI requestURI = exchange.getRequestURI();
            String[] pathSegments = requestURI.getPath().substring(1).split("/");
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines()
                    .collect(Collectors.joining("\n"));



            //handle request
            content = " ";

            if(pathSegments.length > 0 && pathSegments[0].equals("tasks")) {

                if(method.equals("GET")){
                     if(pathSegments.length==1){  //리스트 조회하기 - GET /tasks
                         readTODOList();
                     }else{   //상세 조회하기 - GET /tasks/{id}
                         readTODO(pathSegments);
                     }
                }else if(method.equals("POST")){
                    createTODO(body);
                }else if(method.equals("PUT") || method.equals("PATCH")){
                    updateTODO(pathSegments,body);
                }else if(method.equals("DELETE")){
                    deleteTODO(pathSegments);
               }else{
                    statusCode = 400;
                }

            }else{
                statusCode = 404;
            }

            //write response
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());

    }

    //read
    private void readTODO(String[] pathSegments){

        try{
            Task task = getTask(getTaskId(pathSegments));
            content = toJSON(task);
            statusCode = 200;
        }catch(IllegalArgumentException exception){
            statusCode = 400;
        }catch(NoSuchElementException exception){
            statusCode = 404;
        }catch(IOException exception){
            statusCode = 500;
        }

    }


    private void readTODOList() {

        try{
            //content = toJSON(taskMap.values());
            //content = toJSON(getTaskListSortedByTaskTitle());
            content = toJSON(getTaskListSortedByTaskId());

            statusCode = 200;
        }catch(IOException exception){
            statusCode = 500;
        }
    }

    //create
    private void createTODO(String body){

        try{
            if(!body.isEmpty()){
                Task task = toTask(body);
                task.setId(taskId++);
                taskMap.put(task.getId(), task);

                content = toJSON(task);
                statusCode = 201;

            }else{
                statusCode = 400;
            }

        }catch(IOException exception){
            statusCode = 500;
        }

    }


    //update
    private void updateTODO(String[] pathSegments, String body) {

        try{
            Task task = getTask(getTaskId(pathSegments));
            if (!body.isEmpty()) {
                task.setTitle(toTask(body).getTitle());
            }

            content = toJSON(task);
            statusCode = 200;

        }catch(IllegalArgumentException exception){
            statusCode = 400;
        }catch(NoSuchElementException exception){
            statusCode = 404;
        }catch(IOException exception){
            statusCode = 500;
        }

    }

    //delete
    private void deleteTODO(String[] pathSegments) {

        try{
            Task task = getTask(getTaskId(pathSegments));
            taskMap.remove(task.getId());
            statusCode = 204;

        }catch(IllegalArgumentException exception){
            statusCode = 400;
        }catch(NoSuchElementException exception){
            statusCode = 404;
        }


    }





    public List<Task> getTaskListSortedByTaskId(){

        List<Long> keySet = new ArrayList<>(taskMap.keySet());

        Collections.sort(keySet);
        //Collections.reverse(keySet);

        List<Task> taskList = new ArrayList<>();

        for(Long key : keySet){
            taskList.add(taskMap.get(key));
        }

        return taskList;
    }



    public List<Task> getTaskListSortedByTaskTitle(){

        List<Long> keySet = getSortedTaskKeyList(
             (k1,k2) -> taskMap.get(k1).getTitle().compareTo(taskMap.get(k2).getTitle())
        );

        List<Task> taskList = new ArrayList<>();

        for(Long key : keySet){
            taskList.add(taskMap.get(key));
        }

        return taskList;
    }



    public List<Long> getSortedTaskKeyList  (Comparator comparator){
        List<Long> keySet = new ArrayList<>(taskMap.keySet());
        keySet.sort(comparator);
        return keySet;
    }




    //get task by id
    private Task getTask(Long taskId){
        return getOptionalTask(taskId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 task id 입니다."));
    }

    private Optional<Task> getOptionalTask(Long taskId){
        Optional<Task> task = Optional.empty();

        if(taskMap.containsKey(taskId)){
            task = Optional.of(taskMap.get(taskId));
        }

        return task;
    }


    private Long getTaskId(String[] pathSegments){
        return getOptionalTaskId(pathSegments).orElseThrow(() -> new IllegalArgumentException("잘못된 형식의 path 입니다."));
    }

    private Optional<Long> getOptionalTaskId(String[] pathSegments){
        Optional<Long> taskId = Optional.empty();

        if (pathSegments.length > 1 && pathSegments[1].matches("^[0-9]+$")) {
            taskId = Optional.of(Long.parseLong(pathSegments[1]));
        }

        return taskId;
    }


    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


    private String toJSON(Object object) throws IOException{
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream,object);
        return outputStream.toString();
    }


}
