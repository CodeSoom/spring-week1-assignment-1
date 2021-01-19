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

    @Test
    void test_id값얻기(){
        String[] array = path.split("/");
        Long id = Long.valueOf(array[2]);
        assertEquals(array[1], "tasks");
        assertEquals(id, 1L);
    }

}
