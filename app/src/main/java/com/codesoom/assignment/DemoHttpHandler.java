package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    DemoHttpHandler() {
        Task task = new Task();
        // Long 타입은 L을 붙여준다
        task.setId(1L);
        task.setTitle("제목 입니다");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // HttpExchange 클래스는 http 관리하는듯. req + res?

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String content = "it's content";

        if (method.equals("GET") && path.equals("/tasks")) {
            System.out.println(method + ", " + path);

            // content 타입은 String 이므로 toString()
            content = tasksToJson();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            System.out.println(method + ", " + path);
            content = "create a new task";
        }


        // response Code, response Length 필수 필수인듯.
        exchange.sendResponseHeaders(200, content.getBytes().length);

        // response 에 사용할 body 객체 생성?
        // request 는 InputStream 을 사용하는듯하고 response 는 OutputStream 사용하는듯?
        OutputStream outputStream = exchange.getResponseBody();

        // outputStream 에 내용을 기입하는듯
        outputStream.write(content.getBytes());

        // outputStream 내용을 출력하는듯
        outputStream.flush();

        // outputStream 에 관련된 리소스 모두 해제
        outputStream.close();
    }

    public String tasksToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();

        // writeValue 는 mapper 가 tasks 를 바인딩해서 outputStream 에 write 해주는 메서드 인가?
        // 뒤에 outputStream.write 가 있는데 왜 여기서도 outputStream ...
        // 데이터를 전달 할 때 마다 outputStream 이 필요한건가..
        // ByteArrayOutputStream 는 tasks 를 전달하기 위한 stream 이고 뒤에 outputStream 은 ResponseBody 에 데이터를 담기 위한 outputStream 인가.
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}

