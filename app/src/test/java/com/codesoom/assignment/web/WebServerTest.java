package com.codesoom.assignment.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebServerTest {
    WebServer server;

    @BeforeEach
    void openServer() {
        assertDoesNotThrow(() -> {
            server = new WebServer(8000);
        });
        server.start();
    }

    @AfterEach
    void closeServer() {
        server.close();
    }

    @Test
    void getRoot() {
        HttpUriRequest request = new HttpGet("http://localhost:8000");
        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

            assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        });
    }
}
