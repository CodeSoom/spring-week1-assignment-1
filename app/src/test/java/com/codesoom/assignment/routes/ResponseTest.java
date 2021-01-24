package com.codesoom.assignment.routes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {
    @Test
    void createTest() {
        final String content = "sample";
        Response response = new Response(Response.STATUS_OK, content);

        assertEquals(Response.STATUS_OK, response.statusCode());
        assertEquals(content, response.content());
    }
}
