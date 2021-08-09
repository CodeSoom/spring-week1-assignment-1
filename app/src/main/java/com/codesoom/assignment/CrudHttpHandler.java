package com.codesoom.assignment;

import com.codesoom.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrudHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private Long autoId = 1L; // 아이디를 자동으로 부여하기 위한 변수


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        OutputStream outputStream = exchange.getResponseBody();

        String response = "[]";


        /**
         * method에 null이 들어온다면 비교주체자가 null이 되버리기 때문에 equals를 실행할 수 없어 NPE가 발생할 가능성이 생기는 것 같습니다.
         * 반대로 "GET".equals(method) 로 변경하면 비교하는 주체가 null이 발생할 일이 없어지기 때문에 NPE 방지가 되는 원리 같습니다.
         */
        if("GET".equals(method) && path.equals("/task")) {
            if(!tasks.isEmpty()) {
                response = toTaskJson();
            }
            exchange.sendResponseHeaders(200,response.getBytes().length);
        }
        else if("POST".equals(method) && path.equals("/task")) {

            InputStream inputStream = exchange.getRequestBody();
            String content = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            Task task = toTask(content);
            task.setId(autoId++);
            tasks.add(task);

            response=toTaskJson();
            exchange.sendResponseHeaders(201,response.getBytes().length);
        }
        else{
            /**
             * 서버가 요청을 완료했다는 뜻을 담고 있습니다.
             * https://datatracker.ietf.org/doc/html/rfc7231#section-6.3.1
             */
            exchange.sendResponseHeaders(200, 0);
        }

        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    private String toTaskJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks);

        return outputStream.toString();
    }

}
