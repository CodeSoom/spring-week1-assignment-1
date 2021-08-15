package com.codesoom.assignment.controllers;

import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * "/tasks/{id}" 경로로 HttpRequest를 받았을때,
 * 데이터를 처리하고, HttpResponse를 전송하는 클래스
 */

public class IdController extends Controller {
    private static final String EMPTY_BODY = "";

    /**
     * GET 처리하는 메서드
     * 인자로 받은 task id를 통하여 task 개체를 가져와 HttpResponse로 전송한다.
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param taskId task id
     * @throws IOException
     */
    public void handleGet(final HttpExchange exchange, final Long taskId) throws  IOException {
        try {
            final Task task = TASK_SERVICE.getTask(taskId);
            sendObject(exchange, HttpURLConnection.HTTP_OK, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * PATCH/PUT 처리하는 메서드
     * 인자로 받은 task id를 통하여 task 개체를 가져와
     * 인자로 받은 HttpRequest body의 값으로 수정한다.
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param taskId task id
     * @param requestBody 수정할 내용
     * @throws IOException
     */
    public void handlePatchOrPut(final HttpExchange exchange, final Long taskId, final String requestBody) throws IOException {
        try {
            final Task task = TASK_SERVICE.updateTask(taskId, requestBody);
            sendObject(exchange, HttpURLConnection.HTTP_OK, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }

    /**
     * DELETE 처리하는 메서드
     * 인자로 받은 task id를 통하여 task 개체를 가져와 삭제한다.
     *
     * @param exchange exchange를 통해 HttpResponse를 전송한다.
     * @param taskId task id
     * @throws IOException
     */
    public void handleDelete(final HttpExchange exchange, final Long taskId) throws IOException {
        try {
            TASK_SERVICE.deleteTask(taskId);
            sendResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT, EMPTY_BODY);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }
}
