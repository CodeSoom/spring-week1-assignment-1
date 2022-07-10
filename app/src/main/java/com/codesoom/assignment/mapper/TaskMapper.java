package com.codesoom.assignment.mapper;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

/**
 * Task를 매핑하는 기능을 가진 클래스입니다.
 */
public class TaskMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 요청된 본문을 HashMap으로 변환해서 리턴합니다.
     *
     * @param request 요청된 본문
     * @return 변환된 HashMap 리턴
     * @throws JsonProcessingException Json 변환에 문제가 발생할 경우 던집니다.
     */
    public static HashMap<String, String> getRequestMap(String request) throws JsonProcessingException {
        return objectMapper.readValue(request, new TypeReference<>() {});
    }

    /**
     * Tasks를 Json String으로 변환하여 리턴
     *
     * @return 변환된 문자열을 리턴
     * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
     **/
    public static String tasksToJson(List<Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }

    /**
     * Task를 Json String으로 변환하여 리턴
     *
     * @param task 변환할 Task
     * @return 변환된 문자열을 리턴
     * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
     **/
    public static String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
