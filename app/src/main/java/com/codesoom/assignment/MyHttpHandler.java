package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class MyHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // request로 어떤 메서드가 들어오는지 확인
        String method = exchange.getRequestMethod();
        System.out.println("method : " + method);

        // response 처리
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌

    }
}
