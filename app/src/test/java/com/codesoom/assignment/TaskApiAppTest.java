package com.codesoom.assignment;

import com.codesoom.assignment.domain.http.HttpResponse;
import com.codesoom.assignment.domain.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

class TaskApiAppTest {

    final String LOCAL_HOST_TASKS = "http://localhost:8000/tasks";
    final String inputJson = "{\"title\": \"아무것도 안하기\"}";

    @BeforeAll
    public static void startServer() throws IOException {
        TaskApiApp.startLocalHttpServer();
    }

    @Test
    @DisplayName("태스크를 등록하지 않고, Task 목록 조회 API 를 호출하면 빈 배열(\"[]\")과 상태 200을 반환한다.")
    public void getEmptyTasks() throws IOException {
        HttpResponse httpResponse = getHttpResponse(new HttpGet(LOCAL_HOST_TASKS));
        Assertions.assertEquals(httpResponse.getContent(), "[]");
        Assertions.assertEquals(httpResponse.getStatusCode(), 200);
    }

    @Test
    @DisplayName("태스크를 등록하고, Task 목록 조회 API 를 호출하면 등록된 Task 들과 상태 200을 반환한다.")
    public void getTasks() throws IOException {
        saveTask();
        saveTask();
        HttpResponse httpResponse = getHttpResponse(new HttpGet(LOCAL_HOST_TASKS));
        String content = httpResponse.getContent();
        System.out.println("content = " + content);

        HttpResponse httpResponse2 = getHttpResponse(new HttpGet(LOCAL_HOST_TASKS + "/2"));
        String content1 = httpResponse2.getContent();
        System.out.println("content1 = " + content1);
    }

    @Test
    @DisplayName("Task 등록 API 를 호출하면, 등록된 Task 와 상태 201을 반환한다.")
    public void postTask() throws IOException {
        HttpResponse httpResponse = saveTask();
        Assertions.assertEquals(httpResponse.getContent(), "{\"id\":1,\"title\":\"아무것도 안하기\"}");
    }

    private HttpResponse saveTask() throws IOException {
        HttpPost httpRequest = new HttpPost(LOCAL_HOST_TASKS);
        StringEntity requestEntity = new StringEntity(inputJson, ContentType.APPLICATION_JSON);
        httpRequest.setEntity(requestEntity);

        return getHttpResponse(httpRequest);
    }

    private HttpResponse getHttpResponse(HttpUriRequest httpRequest) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpRequest);
        InputStream content = response.getEntity().getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));

        return new HttpResponse(
                bufferedReader.lines().collect(Collectors.joining("\n")),
                response.getStatusLine().getStatusCode()
        );
    }
}