package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class TaskService {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<Long, Task> tasks = new HashMap<>();
    private Long autoId = 1L; // 아이디를 자동으로 부여하기 위한 변수

    public SendResponseData getAll() throws IOException {

        if(!tasks.isEmpty()) {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),toTaskJson());
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(),"[]");
        }

    }

    public SendResponseData getOne(String hasId) throws IOException{

        Task task = tasks.get(Long.parseLong(hasId));
        if(task==null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
           // NOT_FOUND();
        } else {
            return new SendResponseData(HttpStatusCode.OK.getStatus(), toTaskJsonOne(task));
        }
    }

    public SendResponseData join(String content) throws IOException {

        Task task = toTask(content);
        task.setId(autoId++);
        tasks.put(task.getId(), task);

        return new SendResponseData(HttpStatusCode.CREATED.getStatus(), toTaskJsonOne(task));

    }

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

    public SendResponseData delete(String findId) throws IOException {

        if(tasks.get(Long.parseLong(findId)) == null) {
            return new SendResponseData(HttpStatusCode.NOT_FOUND.getStatus(), "[]");
        } else {
            tasks.remove(Long.parseLong(findId));
            return new SendResponseData(HttpStatusCode.NO_CONTENT.getStatus(), "[]");
        }

    }

    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    private String toTaskJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks.values());

        return outputStream.toString();
    }

    private String toTaskJsonOne(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,task);

        return outputStream.toString();
    }
}
