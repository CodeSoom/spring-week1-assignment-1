package com.codesoom.assignment.utils;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ParseUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 할일을 json 형태로 파싱한다.
     */
    public static String parseTaskToJsonString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

    /**
     * 할일 목록을 json 형태로 파싱한다.
     */
    public static String parseTaskListToJsonString(List<Task> taskList) throws JsonProcessingException {
        return objectMapper.writeValueAsString(taskList);
    }

}
