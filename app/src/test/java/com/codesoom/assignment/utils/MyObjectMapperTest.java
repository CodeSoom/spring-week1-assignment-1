package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class MyObjectMapperTest {
    MyObjectMapper myObjectMapper = new MyObjectMapper();

    @Test
    @DisplayName("Task 객체를 주었을 때 JSON 으로 변하는지")
    public void parseTaskToJSON() throws IllegalAccessException {
        Task task1 = new Task(1L, "아무것도 안 하기");
        String s = myObjectMapper.getJson(task1);
        System.out.println("s = " + s);
    }

    @Test
    @DisplayName("Task Collection 을 주었을 때 JSON 으로 변하는지")
    public void parseTasksToJSON() throws IllegalAccessException {
        Map<Long, Task> tasks = new HashMap<>();
        Task task1 = new Task(1L, "아무것도 안 하기");
        Task task2 = new Task(2L, "아무것도 안 하기");

        tasks.put(1L, task1);
        tasks.put(2L, task2);

        String jsonArray = myObjectMapper.getJsonArray(tasks.values());
        System.out.println("jsonArray = " + jsonArray);
    }

}