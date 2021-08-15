package com.codesoom.assignment.controllers;

import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * "/tasks" 경로로 HttpMethod가 들어왔을 때
 * 데이터를 처리하고 HttpResponse를 전송하는 클래스
 */
public class TaskController extends Controller {
    private static final String TO_TASK_FAIL = "Task conversion fail.";

    /**
     * GET 처리하는 메서드
     * 현재 저장되어 있는 task들을 출력한다.
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @throws IOException
     */
    public void handleGet(final HttpExchange exchange) throws  IOException {
        sendObject(exchange, HttpURLConnection.HTTP_OK, TASK_SERVICE.getTasks());
    }

    /**
     * POST 처리하는 메서드
     * title="<문자열>" 형식의 값을 body로 받는다.
     * task를 생성 및 저장하고, 저장한 task를 출력한다.
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param requestBody 저장할 task 개체의 내용
     * @throws IOException
     */
    public void handlePost(final HttpExchange exchange, final String requestBody) throws IOException {
        try {
            final Task task = TASK_SERVICE.createTask(requestBody);
            sendObject(exchange, HttpURLConnection.HTTP_CREATED, task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_TASK_FAIL);
        }
    }
}
