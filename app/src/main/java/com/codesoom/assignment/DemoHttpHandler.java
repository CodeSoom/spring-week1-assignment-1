package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DemoHttpHandler implements HttpHandler {
    //Object
    private List<Task> tasks = new ArrayList<>();

    //Method
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        //Init
        String content = "Nothing 404";
        String targetPath = "";
        String requestId = "";
        Task targetTask = new Task();
        Map<String, String> bodyContent = new HashMap<>();
        if(!body.isBlank() && body.contains("=")){
            bodyContent = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(body);
        }

        //Path Parsing
        Path path = Paths.get(uri.getPath());
        if(path.getNameCount() > 0){
            targetPath = path.getName(0).toString();
        }
        if(path.getNameCount() > 1){
            requestId = path.getName(1).toString();
            final String id = requestId;
            Optional<Task> stream = tasks.stream().filter(obj -> id.equals(obj.getId().toString())).findFirst();
            if(stream.isPresent()){
                targetTask = stream.get();
            }
        }

        System.out.println("method : " + method + ", body : " + body);
        System.out.println("targetPath : " + targetPath + ", requestId : " + requestId);

        if("tasks".equals(targetPath) && "GET".equals(method)){
            if(requestId.isBlank()){
                content = objectMapper.writeValueAsString(tasks);
            }else{
                content = objectMapper.writeValueAsString(targetTask);
            }

        }else if("tasks".equals(targetPath) && "POST".equals(method)){
            String title = bodyContent.get("title");
            if(!title.isEmpty()){
                int newId = 1;
                if(tasks.size() != 0){
                    tasks.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                    newId = tasks.get(0).getId()+1;
                }

                Task task = new Task();
                task.setId(newId);
                task.setTitle(title);
                tasks.add(task);
                content = objectMapper.writeValueAsString(task);
            }
        }else if("tasks".equals(targetPath) && "PUT".equals(method)){
            String title = bodyContent.get("title");
            if(!title.isEmpty() && !requestId.isBlank()){
                targetTask.setTitle(title);
            }
            content = objectMapper.writeValueAsString(targetTask);
        }else if("tasks".equals(targetPath) && "DELETE".equals(method)){
            if(!requestId.isBlank()){
                final String id = requestId;
                tasks.removeIf(obj -> id.equals(obj.getId().toString()));
            }
        }
        exchange.sendResponseHeaders(200, content.getBytes().length); // 영어, 한글 byte길이가 다르므로 byte[]로 넘김.
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush(); //버퍼를 모두 출력하고 비우는 역할.
        outputStream.close();
    }
}