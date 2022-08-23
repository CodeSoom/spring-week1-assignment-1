package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

/**
 * @ClassName IAssignmentHttpHandler
 * @Description API에 필요한 controller를 담당하는 클래스의 메소드를 정의해 놓은 인터페이스이다. response와 CRUD에 필요한 로직 호풀을 담당하는 역할을 한다.
 */

public interface IAssignmentController {

    void create(HttpExchange exchange, InputStream inputStream) throws IOException;

    void getAll(HttpExchange exchange) throws IOException;

    default void response(HttpExchange exchange, String content, Integer statusCode)
        throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    default String toBody(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    default void bodyIsNotNullOrThrow(String body, HttpExchange exchange)
        throws IOException {
        if (body.isEmpty()) {
            String content = "Empty body";
            this.response(exchange, content, 400);
        }
    }
}
