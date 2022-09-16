package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 응답 관련 데이터를 처리하는 유틸 클래스
 */
public class ResponseUtils {

    /**
     * 요청 작업에 실패한 경우 에러 코드와 함께 응답을 생성합니다.
     * @param exchange
     * @param httpStatus
     * @throws IOException
     */
    public static void sendError(HttpExchange exchange, HttpStatus httpStatus) throws IOException {
        try (OutputStream responseBody = exchange.getResponseBody()) {
            String message = "Request was not processed!";
            exchange.sendResponseHeaders(httpStatus.getCode(), message.getBytes().length);
            responseBody.write(message.getBytes());
        }
    }

    /**
     * 요청을 성공적으로 처리한 경우 보낼 응답을 생성합니다.
     * @param exchange
     * @param content
     * @param httpStatus
     * @throws IOException
     */
    public static void sendResponse(HttpExchange exchange, String content, HttpStatus httpStatus) throws IOException {
        try (OutputStream responseBody = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(httpStatus.getCode(), content.getBytes().length);
            responseBody.write(content.getBytes());
        }
    }
}
