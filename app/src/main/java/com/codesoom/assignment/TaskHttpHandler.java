package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.router.*;
import com.codesoom.assignment.service.Parser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 *  Task의 로직을 가지고 있고 관련된 Http 요청을 처리하는 클래스
 */
public class TaskHttpHandler implements HttpHandler {
    private final Router router = new Router();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (method.equals("GET")) {
            router.getHandle(exchange, path);
            return;
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            router.postHandle(exchange);
            return;
        }

        if (method.equals("PUT") && Parser.isDetailMatches(path)) {
            router.putHandle(exchange, path);
            return;
        }

        if (method.equals("DELETE") && Parser.isDetailMatches(path)) {
            router.deleteHandle(exchange, path);
            return;
        }

        sendResponse(exchange, HttpStatus.OK, -1);
    }

    public static void sendResponse(HttpExchange exchange, HttpStatus httpStatus, int responseLength) throws IOException {
        exchange.sendResponseHeaders(httpStatus.getCode(), responseLength);
    }

    /**
     * 응답할 본문을 받아 응답 본문에 담아 보냅니다.
     *
     * @param exchange 응답을 담을 본문을 가진 파라미터
     * @param content 응답 내용
     * @throws IOException 입출력이 잘못될 경우 던집니다.
     */
    public static void writeResponseBody(HttpExchange exchange, String content) throws IOException {
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }
}
