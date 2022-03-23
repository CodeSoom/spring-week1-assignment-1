package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.HttpRequest;
import com.codesoom.assignment.domain.HttpResponse;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.exception.NoSuchTaskException;
import com.codesoom.assignment.exception.TooManyPathVariablesException;
import com.codesoom.assignment.exception.WrongTaskJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.codesoom.assignment.domain.HttpMethod.*;
import static java.nio.charset.StandardCharsets.*;


public class TaskHttpHandler implements HttpHandler {

    private final List<Task> tasks = new ArrayList<>();
    // TODO: [later] ObjectMapper 는 쓰지 말라던데 ...직접 구현해보자.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            response(exchange, handleTask(new HttpRequest(exchange)));
        } catch (TooManyPathVariablesException | WrongTaskJsonException | NoSuchTaskException | IllegalArgumentException e) {
            response(exchange, new HttpResponse(e.getMessage(), 400));
        } catch (Exception e) {
            response(exchange, new HttpResponse(e.getMessage(), 500));
        }
    }

    private void response(HttpExchange exchange, HttpResponse httpResponse) {
        try(OutputStream responseBody = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getContent().getBytes(UTF_8).length);
            responseBody.write(httpResponse.getContent().getBytes(UTF_8));
            responseBody.flush();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private HttpResponse handleTask(HttpRequest httpRequest) {
        // TODO: Task 수정

        switch (httpRequest.getMethod()) {
            // Task 생성
            case POST -> {
                Task task = parseJsonToTask(httpRequest.getBody());

                tasks.add(task);
                return new HttpResponse(parseTaskToJson(task), 201);
            }
            // Task 목록 조회
            case GET -> {
                // pathVariable 이 올바르게 입력되었고 존재하는 경우
                if(httpRequest.hasPathVariable()) {
                    return processPathVariableTaskIdWithStrategy(httpRequest
                            , task -> new HttpResponse(parseTaskToJson(task), 200));
                }

                return new HttpResponse(parseTasksToJson(tasks), 200);
            }
            // Task 삭제
            case DELETE -> {
                if(httpRequest.hasPathVariable()) {
                    return processPathVariableTaskIdWithStrategy(httpRequest ,
                            task -> {
                                tasks.remove(task);
                                return new HttpResponse("task id: " + task.getId() + "가 삭제되었습니다.", 200);
                            }
                    );
                }

                throw new IllegalArgumentException("삭제할 task ID를 입력해야 합니다. ex) .../tasks/1");
            }
        }

        return new HttpResponse("지원하지 않는 메서드입니다.", 405);
    }

    private HttpResponse processPathVariableTaskIdWithStrategy(HttpRequest httpRequest, Function<Task, HttpResponse> taskProcessStrategy) {
        if(httpRequest.getPathVariables().length > 1) {
            throw new TooManyPathVariablesException("경로가 잘못되었습니다. 올바른 예) .../tasks/1");
        }

        int taskId = getTaskIdFromPathVariables(httpRequest.getPathVariables());

        return findTaskAndResponse(taskId, taskProcessStrategy);
    }

    private HttpResponse findTaskAndResponse(int taskId, Function<Task, HttpResponse> responseFunction) {
        return tasks.stream()
                .filter((task) -> task.getId().intValue() == taskId)
                .findFirst()
                .map(responseFunction)
                .orElseThrow(() -> new NoSuchTaskException("task id: " + taskId + "는 존재하지 않습니다."));
    }

    private int getTaskIdFromPathVariables(String[] pathVariables) {
        int taskId;
        try {
            taskId = Integer.parseInt(pathVariables[0]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id는 숫자로 입력하셔야 합니다. 올바른 예) .../tasks/1");
        }
        return taskId;
    }

    private String parseTaskToJson(Task task) {
        try {
            return objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Task 객체를 JSON 문자열로 변환하는데 실패하였습니다.");
        }
    }

    private String parseTasksToJson(Collection<Task> tasks) {
        try {
            return objectMapper.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Tasks 컬렉션 객체를 JSON 문자열로 변환하는데 실패하였습니다.");
        }
    }

    private Task parseJsonToTask(String body) {
        try {
            return objectMapper.readValue(body, Task.class);
        } catch (JsonProcessingException e) {
            throw new WrongTaskJsonException("잘못된 JSON 형식입니다. 올바른 예) { \"title\": \"과제 제출하기\" }");
        }
    }
}
