package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 루트 경로("/")의 핸들러입니다. 이 외의 경로로 요청이 들어오면
 * {@link com.codesoom.assignment.http.HttpStatus#NOT_FOUND}를 반환합니다.
 *
 * @author Changsu Im
 * @date 2021-05-05
 * @since 0.1.0
 */
public class ContextRootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        if (!path.equals("/")) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.code(), 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.close();
            return;
        }

        final String content = "Hello, World!";
        exchange.sendResponseHeaders(HttpStatus.OK.code(), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
        // exchange.close();
    }
}
