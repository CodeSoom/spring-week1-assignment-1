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
    }

}
