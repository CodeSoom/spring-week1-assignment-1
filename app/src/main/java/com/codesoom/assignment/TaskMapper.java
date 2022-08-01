package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Task 객체와 Data를 변환해줍니다
 */
public class TaskMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON String을 Task 객체로 변환합니다
     * @param content JSON String
     * @return Task 객체
     * @throws JsonProcessingException JSON 에서 Task 로 변환 중에 발생할 수 있는 예외
     */
    public Task readTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    /**
     * Task 객체를 JSON String으로 변환합니다
     * @param task JSON String 으로 변환할 Task 객체
     * @return JSON String
     * @throws JsonProcessingException Task 에서 String 으로 변환 중에 발생할 수 있는 예외
     */
    public String writeTaskAsString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

    /**
     * Task List를 JSON String으로 변환합니다
     * @param tasks JSON String 으로 변환할 Task List
     * @return JSON String
     * @throws JsonProcessingException Task List 에서 String 으로 변환 중에 발생할 수 있는 예외
     */
    public String writeTasksAsString(List<Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }
}
