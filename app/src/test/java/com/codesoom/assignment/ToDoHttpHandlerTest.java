package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ToDoHttpHandlerTest {

    @Test
    @DisplayName("tasks 가 비어있고 GET /tasks 요청하면 []을 리턴합니다")
    public void givenEmptyTasks_whenGetTasks_thenReturnEmptyArray() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("GET");
        URI uri = new URI("http://localhost:8000/tasks");
        httpExchange.setRequestURI(uri);

        // when
        new ToDoHttpHandler().handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("[]", responseBody.toString());
        assertEquals(200, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("POST /tasks 요청하면 등록 성공한 Task 객체를 JSON으로 리턴합니다")
    public void givenEmptyTasks_whenPostTask_thenReturnTaskJSON() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("POST");
        URI uri = new URI("http://localhost:8000/tasks");
        httpExchange.setRequestURI(uri);
        httpExchange.setRequestBody("{\"title\":\"test_title\"}");

        // when
        new ToDoHttpHandler().handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("{\"id\":0,\"title\":\"test_title\"}", responseBody.toString());
        assertEquals(201, httpExchange.getResponseCode());
    }

}
