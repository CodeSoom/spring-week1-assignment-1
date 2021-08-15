package com.codesoom.assignment.controllers;

import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * "/tasks" GET POST
 * "/tasks/{id}" GET PUT PATCH DELETE를 제외한 HttpRequest를 처리하는 클래스
 */

public class ExceptionController extends Controller {
    /**
     * "/tasks" GET POST
     * "/tasks/{id}" GET PUT PATCH DELETE 를 제외한 나머지 메서드 HttpRequest요청이 온경우
     * "<경로> can only handle <처리할 수 있는 메서드들> methods." 에러메시지 전송
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param path HttpRequest가 들어온 url 경로
     * @param allowedMethods 해당 경로에서 허용되는 메서드 배열
     * @throws IOException
     */
    public void handleInvalidMethod(
            final HttpExchange exchange, final String path, final String[] allowedMethods
    ) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        final Consumer<String> methodAppender = method -> stringBuilder.append(method)
                        .append(" ");
        stringBuilder.append(TaskHandler.HANDLER_PATH)
                    .append(path)
                    .append(" can only handle ");
        Arrays.stream(allowedMethods)
                .forEach(methodAppender);
        stringBuilder.append("methods.");
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD, stringBuilder.toString());
    }

    /**
     * "/tasks" "/tasks/{id}"를 제외한 경로로 HttpRequest가 들어온경우
     * "Invalid request." 에러메시지 전송
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @throws IOException
     */
    public void handleInvalidRequest(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_REQUEST);
    }

    /**
     * "/tasks/{id}" 에서 id값이 잘못된 HttpRequest가 들어온경우
     * "Invalid id." 에러메시지 전송
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @throws IOException
     */
    public void handleInvalidId(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_ID);
    }
}
