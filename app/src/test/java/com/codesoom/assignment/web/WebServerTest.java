package com.codesoom.assignment.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getAllEmptyTask() {
        HttpUriRequest request = new HttpGet("http://localhost:8000/tasks");
        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
            String responseBody = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()))
                    .lines()
                    .collect(Collectors.joining(""));

            assertEquals(200, httpResponse.getStatusLine().getStatusCode());
            assertNotNull(httpResponse.getEntity().getContentType());
            assertEquals("[]", responseBody);
        });
    }

    @Test
    void createNewTask() {
        HttpPost post = new HttpPost("http://localhost:8000/task");
        String requestBody = "{\"title\": \"Play Game\"}";
        assertDoesNotThrow(() -> {
            StringEntity requestEntity = new StringEntity(requestBody);
            post.setEntity(requestEntity);
        });

        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(post);
            String responseBody = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent()))
                .lines()
                .collect(Collectors.joining(""));

            assertEquals(201, httpResponse.getStatusLine().getStatusCode());
            assertNotNull(httpResponse.getEntity().getContentType());
            assertEquals("{\"id\":1,\"title\":\"Play Game\"}", responseBody);
        });
    }

    @Test
    void getSpecificTask() {
        // create task for get.
        HttpPost post = new HttpPost("http://localhost:8000/tasks");
        String requestBody = "{\"title\": \"Play Game\"}";
        assertDoesNotThrow(() -> {
            StringEntity requestEntity = new StringEntity(requestBody);
            post.setEntity(requestEntity);
            HttpClientBuilder.create().build().execute(post);
        });

        HttpUriRequest request = new HttpGet("http://localhost:8000/tasks/0");
        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
            String responseBody = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent()))
                .lines()
                .collect(Collectors.joining(""));

            assertEquals(200, httpResponse.getStatusLine().getStatusCode());
            assertNotNull(httpResponse.getEntity().getContentType());
            assertEquals("{\"id\":1,\"title\":\"Play Game\"}", responseBody);
        });
    }

    @Test
    void getUnCreatedTask() {
        HttpUriRequest request = new HttpGet("http://localhost:8000/tasks/1");
        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
            assertEquals(404, httpResponse.getStatusLine().getStatusCode());
        });
    }

    @Test
    void putUnCreatedTask() {
        HttpPut put = new HttpPut("http://localhost:8000/tasks/1");
        String requestBody = "{\"title\": \"Play Game\"}";
        assertDoesNotThrow(() -> {
            StringEntity requestEntity = new StringEntity(requestBody);
            put.setEntity(requestEntity);

            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(put);
            assertEquals(404, httpResponse.getStatusLine().getStatusCode());
        });
    }
}
