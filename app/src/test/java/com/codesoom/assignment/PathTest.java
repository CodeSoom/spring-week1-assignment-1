package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {

    String path;

    @BeforeEach
    void setUp(){
        path = "/tasks/1";
    }

    /*
    목표 : '/'를 기준으로 문자열을 자르고 id값을 얻는다.
    알아내고 싶은 것 :
    [1] 올바르게 문자열을 분리했는가?
    [2] id값을 올바르게 얻었는가?
    */
    @Test
    void test_id값얻기(){
        String[] array = path.split("/");
        Long id = Long.valueOf(array[2]);
        assertEquals(array[1], "tasks"); // 알아내고 싶은 것 [1]
        assertEquals(id, 1L); // 알아내고 싶은 것 [2]
    }

}
