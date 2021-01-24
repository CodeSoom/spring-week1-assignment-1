package com.codesoom.assignment.models;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Assertions;

@DisplayName("Task 클래스")
public class TaskTest {
    @Nested
    @DisplayName("id 메소드는")
    class Describe_id {

        @Nested
        @DisplayName("id가 null 이 아닐 때")
        class Context_when_id_is_not_null {
            Long givenID = 1L;
            Task givenTask = new Task(givenID, null);

            @Test
            @DisplayName("생성시 id에 입력된 long 타입 정수를 리턴한다.")
            void It_returns_long_type_integer() {
                Assertions.assertEquals(givenID, givenTask.id());
            }
        }

        @Nested
        @DisplayName("id가 null 일 때")
        class Context_when_id_is_null {
            Task givenTask = new Task(null, null);

            @Test
            @DisplayName("null 을 리턴한다.")
            void It_returns_long_type_integer() {
                Assertions.assertNull(givenTask.id());
            }
        }
    }

    @Nested
    @DisplayName("title 메소드는")
    class Describe_title {

        @Nested
        @DisplayName("title 이 null 이 아닐 때")
        class Context_when_title_is_not_null {
            String givenTitle = "sample";
            Task givenTask = new Task(null, givenTitle);

            @Test
            @DisplayName("생성시 title 에 입력된 문자열을 리턴한다.")
            void It_returns_long_type_integer() {
                Assertions.assertEquals(givenTitle, givenTask.title());
            }
        }

        @Nested
        @DisplayName("title 이 null 일 때")
        class Context_when_id_is_null {
            Task givenTask = new Task(null, null);

            @Test
            @DisplayName("null 을 리턴한다.")
            void It_returns_long_type_integer() {
                Assertions.assertNull(givenTask.title());
            }
        }
    }

    @Nested
    @DisplayName("toString 메소드는")
    class Describe_toString {

        @Nested
        @DisplayName("모든 값이 null 이 아닐 때")
        class Context_all_property_is_not_null {
            Long givenID = 1L;
            String givenTitle = "sample";
            Task givenTask = new Task(givenID, givenTitle);
            String expectJSONString = String.format("{\"id\":%d,\"title\":\"%s\"}", givenID, givenTitle);

            @Test
            @DisplayName("모든 값이 포함된 JSON string 을 리턴한다.")
            void It_returns_json_string_has_all_property() {
                Assertions.assertEquals(expectJSONString, givenTask.toString());
            }
        }

        @Nested
        @DisplayName("어떤 값이 null 일 때")
        class Context_some_property_is_null {
            Long givenID = 1L;
            Task givenTask = new Task(givenID, null);
            String expectJSONString = String.format("{\"id\":%d,\"title\":null}", givenID);

            @Test
            @DisplayName("null 인 값이 null 로 입력된 JSON string 을 리턴한다.")
            void It_returns_json_string_with_null_property() {
                Assertions.assertEquals(expectJSONString, givenTask.toString());
            }
        }

        @Nested
        @DisplayName("모든 값이 null 일 때")
        class Context_all_property_is_null {
            Task givenTask = new Task(null, null);
            String expectJSONString = "{\"id\":null,\"title\":null}";

            @Test
            @DisplayName("모든 값이 null 로 입력된 JSON string 을 리턴한다.")
            void It_returns_json_string_all_null_property() {
                Assertions.assertEquals(expectJSONString, givenTask.toString());
            }
        }
    }
}
