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
    @DisplayName("tasks가 2개 있고 GET /tasks 요청하면 2개의 Task를 리턴합니다")
    public void givenThreeTasks_whenGetTasks_thenReturnThreeTasks() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub setupHttpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();
        postTask(setupHttpExchange, httpHandler, "title0");
        postTask(setupHttpExchange, httpHandler, "title1");

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("GET");
        URI uri = new URI("http://localhost:8000/tasks");
        httpExchange.setRequestURI(uri);
        httpHandler.handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("[{\"id\":1,\"title\":\"title0\"},{\"id\":2,\"title\":\"title1\"}]", responseBody.toString());
        assertEquals(200, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("POST /tasks 요청하면 등록 성공한 Task 객체를 JSON으로 리턴합니다")
    public void givenEmptyTasks_whenPostTask_thenReturnTaskJSON() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();

        // when
        postTask(httpExchange, httpHandler, "test_title");

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("{\"id\":1,\"title\":\"test_title\"}", responseBody.toString());
        assertEquals(201, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("POST /tasks/:id 요청하면 해당 id Task 객체를 JSON으로 리턴합니다")
    public void givenAddedTasks_whenGetTaskWithId_thenReturnTaskJSON() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub setupHttpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();
        postTask(setupHttpExchange, httpHandler, "title1");
        postTask(setupHttpExchange, httpHandler, "title2");

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("GET");
        URI uri = new URI("http://localhost:8000/tasks/1");
        httpExchange.setRequestURI(uri);
        httpHandler.handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("{\"id\":1,\"title\":\"title1\"}", responseBody.toString());
        assertEquals(200, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("존재하지 않는 id로 POST /tasks/:id 요청하면 404 status code가 리턴됩니다")
    public void givenEmptyTasks_whenGetTaskWithId_thenReturn404() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();

        // when
        httpExchange.setRequestMethod("GET");
        URI uri = new URI("http://localhost:8000/tasks/0");
        httpExchange.setRequestURI(uri);
        httpHandler.handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("Not found task by id", responseBody.toString());
        assertEquals(404, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("존재하지 않는 id에 PUT /tasks/:id 요청하면 404 status code가 리턴됩니다")
    public void givenEmptyTasks_whenPUTTaskWithId_thenReturn404() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();

        // when
        httpExchange.setRequestMethod("PUT");
        URI uri = new URI("http://localhost:8000/tasks/0");
        httpExchange.setRequestURI(uri);
        httpExchange.setRequestBody("{\"title\":\"hello\"}");
        httpHandler.handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("Not found task by id", responseBody.toString());
        assertEquals(404, httpExchange.getResponseCode());
    }

    @Test
    @DisplayName("존재하는 id로 PUT /tasks/:id 요청하면 변경된 Task가 JSON으로 리턴됩니다")
    public void givenExistedTasks_whenPUTTaskWithId_thenReturn404() throws IOException, URISyntaxException {
        // given
        final HttpExchangeStub setupHttpExchange = new HttpExchangeStub();
        final ToDoHttpHandler httpHandler = new ToDoHttpHandler();
        postTask(setupHttpExchange, httpHandler, "title1");
        postTask(setupHttpExchange, httpHandler, "title2");

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("PUT");
        URI uri = new URI("http://localhost:8000/tasks/1");
        httpExchange.setRequestURI(uri);
        httpExchange.setRequestBody("{\"title\":\"hello\"}");
        httpHandler.handle(httpExchange);

        // then
        final ByteArrayOutputStream responseBody = (ByteArrayOutputStream) httpExchange.getResponseBody();
        assertEquals("{\"id\":1,\"title\":\"hello\"}", responseBody.toString());
        assertEquals(200, httpExchange.getResponseCode());
    }

    private void postTask(HttpExchangeStub httpExchange, ToDoHttpHandler httpHandler, String title) throws URISyntaxException, IOException {
        httpExchange.setRequestMethod("POST");
        URI uri = new URI("http://localhost:8000/tasks");
        httpExchange.setRequestURI(uri);
        httpExchange.setRequestBody("{\"title\":\"" + title + "\"}");
        httpHandler.handle(httpExchange);
    }
}
