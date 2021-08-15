package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 메소드(GET, POST, PUT, DELETE)에 따라 처리하는 클래스
 */
public class TaskService {

    private JsonConverter jsonConverter = new JsonConverter();
    private Map<Long, Task> tasks = new HashMap<>();
    private Long autoId = 1L;

    /**
     * GET - Task 객체의 전체 목록반환
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData getAll() throws IOException {

        if(!tasks.isEmpty()) {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),jsonConverter.toTaskJson(tasks));
        }
        return new SendResponseData(HttpStatusCode.OK.getStatus(),"[]");
    }

    /**
     * Get - 상세조회 Task 객체 1개 반환
     * @param hasId - Task 객체를 찾을 key
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData getOne(String hasId) throws IOException{

        Task task = tasks.get(Long.parseLong(hasId));
        if(task==null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
           // NOT_FOUND();
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(), jsonConverter.toTaskJsonOne(task));
        }
    }

    /**
     * POST - Task 객체를 Map에 추가
     * @param content - Task 객체에 들어갈 데이터
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData join(String content) throws IOException {

        Task task = jsonConverter.toTask(content);
        task.setId(autoId++);
        tasks.put(task.getId(), task);

        return new SendResponseData(HttpStatusCode.CREATED.getStatus(), jsonConverter.toTaskJsonOne(task));

    }

    /**
     * PUT | PATH - Task 객체 데이터 수정
     * @param content 수정할 값을 담고 있는 데이터
     * @param findId Task 객체를 찾을 key
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData edit(String content, String findId) throws IOException {

        Task taskPut = jsonConverter.toTask(content);

        if(tasks.get(Long.parseLong(findId)) == null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
        } else {
            Task task = tasks.get(Long.parseLong(findId));
            task.setTitle(taskPut.getTitle());
            return new SendResponseData(HttpStatusCode.OK.getStatus(), jsonConverter.toTaskJsonOne(task));
        }

    }

    /**
     * DELETE - Task 객체 삭제
     * @param findId - 삭제할 Task 객체의 key
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData delete(String findId) throws IOException {

        if(tasks.get(Long.parseLong(findId)) == null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
        } else {
            tasks.remove(Long.parseLong(findId));
            return new SendResponseData(HttpStatusCode.NO_CONTENT.getStatus(), "[]");
        }

    }

    /**
     * HTTP 상태코드와 응답 메시지를 클라이언트에게 보내주는 메소드
     * @param data - 상태코드와 응답데이터를 가지고 있는 객체
     * @param exchange - sendResponseHeaders 실행시키기 위한 객체
     * @throws IOException
     */
    public void sendResponse(SendResponseData data, HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(data.getHttpStatusCode(),data.getResponse().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(data.getResponse().getBytes());
        outputStream.flush();
        outputStream.close();

    }

}
