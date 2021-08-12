package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    //Object
    private List<Task> tasks = new ArrayList<>();

    //Method
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        String queryString = uri.getQuery();
        String content = "Nothing 404";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        Path path = Paths.get(uri.getPath());

        System.out.println("uri : " +method + "|" + path + "?" + queryString);
        System.out.println("body : " + body);


        String target = "";
        String requestId = "";
        if(path.getNameCount() > 0){
            target = path.getName(1).toString();
        }

        if(path.getNameCount() > 1){
            requestId = path.getName(2).toString();
        }

        if("tasks".equals(target) && "GET".equals(method)){
            content = objectMapper.writeValueAsString(tasks);

        }else if("tasks".equals(target) && "POST".equals(method)){
            String title = body.split("=")[1];
            int newId = 0;
            if(tasks.size() != 0){
                tasks.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                newId = tasks.get(0).getId()+1;
            }
            Task task = new Task();
            task.setId(newId);
            task.setTitle(title);
            tasks.add(task);
            content = objectMapper.writeValueAsString(tasks);

        }else if("tasks".equals(target) && "PUT".equals(method)){
            String title = body.split("=")[1];
            for (Task task : tasks) {
                String taskId = Integer.toString(task.getId());
                if (requestId.equals(taskId)) {
                    task.setTitle(title);
                }
            }
            content = objectMapper.writeValueAsString(tasks);
        }else if("tasks".equals(target) && "DELETE".equals(method)){
            for(int i=0; i<tasks.size(); i++){
                String taskId = Integer.toString(tasks.get(i).getId());
                if(requestId.equals(taskId)){
                    tasks.remove(i);
                }
            }
            content = objectMapper.writeValueAsString(tasks);
        }

        //Client가 요청했으니 뭐라도 Response를 전달해야함. 정상상태 200으로.
        exchange.sendResponseHeaders(200, content.getBytes().length); // 영어, 한글 byte길이가 다르므로 byte[]로 넘김.
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush(); //버퍼를 모두 출력하고 비우는 역할.
        outputStream.close();
    }
}