package com.codesoom.assignment.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TaskConverterJsonToMapTest {

    @Test
    @DisplayName(" JSON형식에 맞지 않아도 [\"]로 감싸준다면 Map에 키 또는 값으로 저장된다")
    void notJson1(){
        // given
        String json = "[\"title1\"=\"테스트1\" ,\"title2\"=\"테스트2\"]";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(json);

        // then
        Assertions.assertEquals(map.get("title1") , "테스트1");
        Assertions.assertEquals(map.get("title2") , "테스트2");
    }

    @Test
    @DisplayName(" [,]로 키:값 Entry가 구분되어 있지 않다면 첫 번째 키와 값만 가져온다")
    void notJson2(){
        // given
        String json = "{\"title1\":\"테스트1\" \"title2\":\"테스트2\"}";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(json);

        // then
        Assertions.assertEquals(map.get("title1") , "테스트1");
    }

    @Test
    @DisplayName("JSON문자열에 키가 중복되지 않는다면 Map에 각각 저장된다")
    void jsonhasMultiKey(){
        // given
        String json = "{\"title1\": \"테스트1\",\"title2\": \"테스트2\"}";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(json);

        // then
        Assertions.assertEquals(map.get("title1"), "테스트1");
        Assertions.assertEquals(map.get("title2"), "테스트2");
    }

    @Test
    @DisplayName("JSON문자열에 숫자 값이 있을 때 구분하지 못 한다")
    void jsonInNumeric(){
        // given
        String json = "{\"id\": 1,\"title\": \"테스트1\"}";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(json);

        // then
        Assertions.assertEquals(map.get("id"), "");
    }

    @Test
    @DisplayName("JSON객체가 여러 개일 때 title은 덮어씌워진다")
    void jsons(){
        // given
        String jsons = "[{\"id\": 1,\"title\": \"테스트1\"},{\"id\": 2,\"title\": \"테스트2\"}]";
        TaskConverter converter = new TaskConverter();

        // when
        Map<String , String> map = converter.jsonToMap(jsons);

        // then
        Assertions.assertEquals(map.get("title") , "테스트2");
    }


}
