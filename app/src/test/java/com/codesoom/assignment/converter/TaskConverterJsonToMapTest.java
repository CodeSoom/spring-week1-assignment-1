package com.codesoom.assignment.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TaskConverterJsonToMapTest {

    @Test
    @DisplayName("JSON문자열이 들어왔을 때")
    void json(){
        // given
        String json = "{\"id\": 1,\"title\": \"테스트1\"}";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(json);

        // then
    }

    @Test
    @DisplayName("JSON이 여러 개일 때")
    void jsons(){
        // given
        String jsons = "[{\"id\": 1,\"title\": \"테스트1\"},{\"id\": 2,\"title\": \"테스트2\"}]";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(jsons);

        // then
    }


}
