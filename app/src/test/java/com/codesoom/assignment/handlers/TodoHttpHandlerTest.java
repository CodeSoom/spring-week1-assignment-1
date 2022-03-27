package com.codesoom.assignment.handlers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class TodoHttpHandlerTest {

    @Test
    void string_split_test() {
//        String reqPath = "/tasks/";
//        String reqPath = "/tasks/123/";
//        String reqPath = "/";
        String reqPath = "/tasks/0/";

        String[] strings = reqPath.split("/");
        System.out.println("strings.length = " + strings.length);
        System.out.println("Arrays.toString(strings) = " + Arrays.toString(strings));
    }

}
