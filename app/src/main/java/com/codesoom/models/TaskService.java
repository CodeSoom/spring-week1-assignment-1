package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TaskService 클래스는 HTTP 메소드(GET, POST, PUT, DELETE)에 따라 처리하는 메소드를 모아둔 클래스입니다.
 * @author HyoungUkJJang(김형욱)
 */
public class TaskService {

    /**
     * Json > Task / Task > Json 으로 변환하기 위한 객체 입니다.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 등록한 Task의 객체들을 편하게 관리하기 위한 객체 입니다.
     * Long - id
     * Task - 등록한 Task 객체
     */
    private Map<Long, Task> tasks = new HashMap<>();

    /**
     * POST 요청이 들어올 때마다 자동으로 고유한 id 값을 부여해주는 변수입니다.
     */
    private Long autoId = 1L;

    /**
     * Task 객체의 전체 목록을 보여주는 GET 메소드를 처리해주는 메소드입니다.
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData getAll() throws IOException {

        if(!tasks.isEmpty()) {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),toTaskJson());
        }
        return new SendResponseData(HttpStatusCode.OK.getStatus(),"[]");
    }

    /**
     * 상세 데이터를 나타내주는 GET 메소드를 처리해주는 메소드입니다.
     * @param hasId - id 값을 매개변수로 Map에서 해당 Task 객체를 가져옵니다.
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData getOne(String hasId) throws IOException{

        Task task = tasks.get(Long.parseLong(hasId));
        if(task==null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
           // NOT_FOUND();
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(), toTaskJsonOne(task));
        }
    }

    /**
     * Task 객체를 등록하기 위한 POST 메소드를 처리해주는 메소드입니다.
     * @param content - Task 객체를 만들기 위한 변수
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData join(String content) throws IOException {

        Task task = toTask(content);
        task.setId(autoId++);
        tasks.put(task.getId(), task);

        return new SendResponseData(HttpStatusCode.CREATED.getStatus(), toTaskJsonOne(task));

    }

    /**
     * Task 객체의 데이터를 수정하기 위한 POST 메소드를 처리해주는 메소드입니다.
     * @param content 수정할 값을 담고 있는 데이터입니다.
     * @param findId 수정할 객체의 id값을 담고 있는 데이터입니다.
     * @return SendResponseData
     * @throws IOException
     */
    public SendResponseData edit(String content, String findId) throws IOException {

        Task taskPut = toTask(content);

        if(tasks.get(Long.parseLong(findId)) == null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
        } else {
            Task task = tasks.get(Long.parseLong(findId));
            task.setTitle(taskPut.getTitle());
            return new SendResponseData(HttpStatusCode.OK.getStatus(), toTaskJsonOne(task));
        }

    }

    /**
     * 클라이언트에게 보내는 응답 데이터로 HTTP 상태코드와 응답 메시지를 보내주는 메소드입니다.
     * @param data
     * @param exchange
     * @throws IOException
     */
    public void sendResponse(SendResponseData data, HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(data.getHttpStatusCode(),data.getResponse().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(data.getResponse().getBytes());
        outputStream.flush();
        outputStream.close();

    }


    /**
     * Task 객체를 지우기 위한 DELETE 메소드를 처리해주는 메소드입니다.
     * @param findId - 삭제할 Task 객체의 id 값을 가지고 있는 변수입니다.
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
     * Json 형태로 들어온 데이터를 Task 객체로 매핑해주는 메소드입니다.
     * @param content
     * @return Task
     * @throws JsonProcessingException
     */
    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    /**
     * Task 객체를 Json 형태로 응답 데이터를 보내주기 위해 매핑해 주는 메소드입니다.
     * @return String
     * @throws IOException
     */
    private String toTaskJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks.values());

        return outputStream.toString();
    }

    /**
     * 상세 데이터 조회를 할때 하나의 Task 객체를 Json 형태로 매핑해주는 메소드입니다.
     * @param task - Json 형태로 변환할 Task 객체
     * @return String
     * @throws IOException
     */
    private String toTaskJsonOne(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,task);

        return outputStream.toString();
    }



}
