package com.codesoom.assignment.controllers;

import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.JsonConverter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * HttpResponse 전송 및 데이터 처리를 담당하는 클래스
 */
public abstract class Controller {
    protected static final String TO_JSON_FAIL = "Json conversion fail.";
    protected static final String INVALID_REQUEST = "Invalid request.";
    protected static final String INVALID_ID = "Invalid id";

    /**
     * 데이터 처리를 담당하는 멤버변수
     */
    protected final static TaskRepository TASK_SERVICE = new TaskRepository();

    /**
     * HttpResponse를 전송하는 메서드
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param statusCode HttpResponse http 상태 코드
     * @param content HttpResponse body에서 전송할 데이터
     * @throws IOException
     */
    protected void sendResponse(
            final HttpExchange exchange, final int statusCode, final String content
    ) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    /**
     * 개체를 Json문자열 형식으로 변환하여 HttpResponse로 전송하는 메서드
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param httpStatusCode HttpResponse http 상태 코드
     * @param object Json문자열로 변환할 개체
     * @throws IOException
     */
    protected void sendObject(final HttpExchange exchange, final int httpStatusCode, final Object object) throws IOException {
        final Optional<String> jsonStringOptional = JsonConverter.toJson(object);
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, httpStatusCode, jsonStringOptional.get());
    }
}
