package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

class MyObjectMapperTest {
    MyObjectMapper myObjectMapper = new MyObjectMapper();

    @Test
    @DisplayName("Task 객체를 주었을 때 JSON 으로 변하는지")
    public void parseTaskToJSON() {
        Task task1 = new Task("아무것도 안 하기");
        String s = myObjectMapper.writeAsString(task1);
        System.out.println("s = " + s);
    }

    @Test
    @DisplayName("Task Collection 을 주었을 때 JSON 으로 변하는지")
    public void parseTasksToJSON() {
        Map<Long, Task> tasks = new HashMap<>();
        Task task1 = new Task("아무것도 안 하기1");
        Task task2 = new Task("아무것도 안 하기2");

        tasks.put(task1.getId(), task1);
        tasks.put(task2.getId(), task2);

        String jsonArray = myObjectMapper.getJsonArray(tasks.values());
        System.out.println("jsonArray = " + jsonArray);
    }

    @Test
    @DisplayName("JSON 내용을 Map 형태로 반환받는지")
    public void testReadJsonAsMap() {
        String example = "{ \"id\": 1, \"title\": \"아무것도 안 하기\" }";
        myObjectMapper.removeCurlyBrackets(example);
    }

    @Test
    @DisplayName("{} 가 없는 JSON 데이터의 프로퍼티를 잘 분리하는지")
    public void testGetEntry() {
        String example = "{ \"id\": 1, \"title\": \"아무것도 안 하기\" }";
        Map<String, String> jsonProperties = myObjectMapper.getJsonPropertyMap(example);
        System.out.println("jsonProperties = " + jsonProperties);
    }

    @Test
    @DisplayName("프로퍼티가 들어있는 Map 을 받아서 Object 로 만들어줌")
    public void testGetObject() {
        String example = "{ \"title\": \"아무것도 안 하기\" }";
        Task object = myObjectMapper.readValue(example, Task.class);
        System.out.println("object = " + object);

        String json = myObjectMapper.writeAsString(object);
        System.out.println("json = " + json);
    }
}