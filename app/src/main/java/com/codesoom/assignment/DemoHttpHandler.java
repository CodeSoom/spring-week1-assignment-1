package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;

/**
 * 자바 HTTP 서버를 만들어보기 위한 데모 핸들러입니다.
 *
 * [GET]     /tasks       : 전체 태스크를 반환합니다.
 * [GET]     /tasks/{id}  : 해당 id의 태스크를 반환합니다.
 * [POST]    /tasks       : 입력받은 태스크를 저장합니다.
 * [PUT]     /tasks/{id}  : 해당 id의 태스크를 수정합니다. (전체를 받아야 함)
 * [PATCH]   /tasks/{id}  : 해당 id의 태스크를 수정합니다. (부분만 받아도 반영해줌)
 * [DELETE]  /tasks/{id}  : 해당 id의 태스크를 삭제합니다.
 */
public class DemoHttpHandler implements HttpHandler {
    private boolean isValidPath(String path) {
        return path.startsWith("/tasks");
    }

    private Long extractID(String path) {
        String[] split = path.split("/");
        return split.length < 3
                ? null
                : Long.parseLong(split[2]);
    }

    private String get(String path) {
        Long id = extractID(path);
        return id != null ? id.toString() : "ALL";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method + " " + uri.getPath());

        OutputStream outputStream = exchange.getResponseBody();
        String content = "";

        if (isValidPath(path)) {
            switch (method) {
                case "GET" -> content = get(path);
                case "POST" -> content = "POST";
                case "PUT" -> content = "PUT";
                case "PATCH" -> content = "PATCH";
                case "DELETE" -> content = "DELETE";
            }

            exchange.sendResponseHeaders(200, content.length());
        } else {
            content = "not found";
            exchange.sendResponseHeaders(404, content.length());
        }

        outputStream.write(content.getBytes());

        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
