package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // HttpExchange 클래스는 http 관리하는듯. req + res?

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String content = "it's content";

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
}

