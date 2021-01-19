package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {

    JSONConverter jsonConverter = new JSONConverter();

    private List<Task> tasks = new ArrayList<>();

    public MyHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task1");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        JSONConverter jsonConverter = new JSONConverter();

        // request로 어떤 메서드가 들어오는지 확인
        String method = exchange.getRequestMethod();

        // path로 어떤 값이 들어오는지 확인
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println("method : " + method + ", path : " + path);

        /*
           JSON 데이터를 받는 부분
            BufferedReader : 인자로 취한 Reader 스트림에 버퍼링 기능을 추가한 클래스
         */
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (!body.isBlank()) {
            System.out.println("body : " + body);

            // JSON 데이터를 task로 변환
            Task task = jsonConverter.JSONToTask(body);
            tasks.add(task);
        }

        // connectionError가 발생하지 않도록 HTTP 상태코드 처리
        // content : 성공적으로 응답 했을 경우, 확인을 위해 화면에 띄워줄 메세지
        String content = "Hello, world!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = jsonConverter.tasksToJSON(tasks);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        // response 처리
        OutputStream outputStream = exchange.getResponseBody(); // getResponseBody() : Response를 byte 배열로 반환
        outputStream.write(content.getBytes());  // 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보냄
        outputStream.flush(); // 버퍼에 남아있는 데이터를 모두 출력시키고 버퍼를 비움
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌

    }

}
