package com.codesoom.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Task > Json | Json > Task 변환클래스
 */
public class JsonConverter {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Json > Task 객체로 매핑
     * @param content - Task 객체에 들어갈 데이터
     * @return Task - 매핑해준 Task 객체 반환
     * @throws JsonProcessingException
     */
    public Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    /**
     * Task > Json 형태로 매핑
     * @return String - 매핑해준 String 객체 반환
     * @throws IOException
     */
    public String toTaskJson(Map tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks.values());

        return outputStream.toString();
    }

    /**
     * 하나의 Task 객체를 Json 형태로 매핑해주는 메소드입니다.
     * @param task - Json 형태로 변환할 Task 객체
     * @return String
     * @throws IOException
     */
    public String toTaskJsonOne(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,task);

        return outputStream.toString();
    }



}
