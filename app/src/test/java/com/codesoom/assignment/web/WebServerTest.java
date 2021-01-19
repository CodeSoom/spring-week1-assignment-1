package com.codesoom.assignment.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebServerTest {
    @BeforeEach
    void openServer() {
        // Open server fot testing.
    }
    @AfterEach
    void closeServer() {
        // Close server fot testing.
    }
    @Test
    void getRoot(){
        HttpUriRequest request = new HttpGet( "http://localhost:8000");
        assertDoesNotThrow(() -> {
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

            assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        });
    }
}
