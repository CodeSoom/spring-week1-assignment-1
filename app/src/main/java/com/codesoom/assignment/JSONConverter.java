package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * JSON 데이터 처리와 관련된 메서드를 관리하기 위한 클래스
 */
public class JSONConverter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String tasksToJson(Map<Long, Task> taskStore) throws IOException {

        // ByteArrayOutputStream() : 바이트 배열 출력 시 사용되는 스트림
        OutputStream outputStream = new ByteArrayOutputStream();

        // Java Object to JSON
        objectMapper.writeValue(outputStream, taskStore);

        // String 형식의 content 변수에 담기 위해 String으로 변환하여 return
        return outputStream.toString();
    }

    public Task jsonToTask(String json) throws JsonProcessingException {
        // json 데이터를 task의 클래스로 변환
        return objectMapper.readValue(json, Task.class);
    }

    public String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        // Java Object to JSON
        objectMapper.writeValue(outputStream, task);

        // String 형식의 content 변수에 담기 위해 String으로 변환하여 return
        return outputStream.toString();
    }

}
