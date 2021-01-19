package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();

    public MyHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task1");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // request로 어떤 메서드가 들어오는지 확인
        String method = exchange.getRequestMethod();

        // path로 어떤 값이 들어오는지 확인
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println("method : " + method + ", path : " + path);


        // connectionError가 발생하지 않도록 HTTP 상태코드 처리
        // content : 성공적으로 응답 했을 경우, 확인을 위해 화면에 띄워줄 메세지
        String content = "Hello, world!";
        exchange.sendResponseHeaders(200, content.getBytes().length);


        // response 처리
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());  // 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보냄
        outputStream.flush(); // 버퍼에 남아있는 데이터를 모두 출력시키고 버퍼를 비움
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌



    }
}
