package com.codesoom.assignment.network;

import com.codesoom.assignment.HttpExchangeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpRouterTest {

    private HttpRouter router;

    @BeforeEach
    public void beforeEach() {
        router = new HttpRouter();
    }

    @Test
    @DisplayName("GET method와 path가 일치하면 executor를 실행합니다")
    public void testGetMethod() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.get("/path1", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("GET");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        router.route(httpExchange);

        // then
        assertEquals(executorSpy.getRequest().getMethod(), HttpMethod.GET);
        assertEquals(executorSpy.getRequest().getPath(), "/path1");
        assertEquals(executorSpy.getResponse().getResponseCode(), 200);
        assertEquals(executorSpy.getResponse().getResponseBody().toString() , "content");
    }

    @Test
    @DisplayName("GET method와 path가 일치하지 않으면 executor가 실행되지 않습니다")
    public void testGetMethod_shouldNotCallExecutor() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.get("/path2", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("GET");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        router.route(httpExchange);

        // then
        assertNull(executorSpy.getRequest());
        assertNull(executorSpy.getResponse());
    }

    @Test
    @DisplayName("POST method와 path가 일치하면 executor를 실행합니다")
    public void testPostMethod() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.post("/path1", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();

        httpExchange.setRequestMethod("POST");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        httpExchange.setRequestBody("{\"title\":\"title1\"}");
        router.route(httpExchange);

        // then
        assertEquals(executorSpy.getRequest().getMethod(), HttpMethod.POST);
        assertEquals(executorSpy.getRequest().getPath(), "/path1");
        assertEquals(executorSpy.getResponse().getResponseCode(), 200);
        assertEquals(executorSpy.getResponse().getResponseBody().toString() , "content");
    }

    @Test
    @DisplayName("POST method와 path가 일치하지 않으면 executor가 실행되지 않습니다")
    public void testPostMethod_shouldNotCallExecutor() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.get("/path2", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.setRequestMethod("POST");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        router.route(httpExchange);

        // then
        assertNull(executorSpy.getRequest());
        assertNull(executorSpy.getResponse());
    }

    @Test
    @DisplayName("PUT method와 path가 일치하면 executor를 실행합니다")
    public void testPutMethod() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.put("/path1", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();

        httpExchange.setRequestMethod("PUT");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        httpExchange.setRequestBody("{\"title\":\"title1\"}");
        router.route(httpExchange);

        // then
        assertEquals(executorSpy.getRequest().getMethod(), HttpMethod.PUT);
        assertEquals(executorSpy.getRequest().getPath(), "/path1");
        assertNotNull(executorSpy.getResponse());
    }

    @Test
    @DisplayName("DELETE method와 path가 일치하면 executor를 실행합니다")
    public void testDeleteMethod() throws URISyntaxException, IOException {
        // given
        HttpRouterExecutorSpy executorSpy = new HttpRouterExecutorSpy((response -> {
            response.send(HttpResponseCode.OK, "content");
        }));
        router.delete("/path1", executorSpy);

        // when
        final HttpExchangeStub httpExchange = new HttpExchangeStub();

        httpExchange.setRequestMethod("DELETE");
        httpExchange.setRequestURI(new URI("http://localhost:8000/path1"));
        router.route(httpExchange);

        // then
        assertEquals(executorSpy.getRequest().getMethod(), HttpMethod.DELETE);
        assertEquals(executorSpy.getRequest().getPath(), "/path1");
        assertEquals(executorSpy.getResponse().getResponseCode(), 200);
        assertEquals(executorSpy.getResponse().getResponseBody().toString() , "content");
    }

}
