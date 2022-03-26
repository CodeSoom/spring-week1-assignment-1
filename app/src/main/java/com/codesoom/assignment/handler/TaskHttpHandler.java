package com.codesoom.assignment.handler;

ㅗimport com.codesoom.assignment.domain.http.HttpRequest;
import com.codesoom.assignment.domain.http.HttpResponse;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.exception.NoSuchTaskException;
import com.codesoom.assignment.exception.TooManyPathSegmentsException;
import com.codesoom.assignment.exception.WrongTaskJsonException;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.MyObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class TaskHttpHandler implements HttpHandler {
    private final MyObjectMapper myObjectMapper = new MyObjectMapper();
    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            response(exchange, handleTask(new HttpRequest(exchange)));
        } catch (TooManyPathSegmentsException | WrongTaskJsonException | NoSuchTaskException | IllegalArgumentException e) {
            response(exchange, new HttpResponse(e.getMessage(), 400));
        } catch (Exception e) {
            response(exchange, new HttpResponse(e.getMessage(), 500));
        }
    }

    private void response(HttpExchange exchange, HttpResponse httpResponse) {
        try(OutputStream responseBody = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getContentLength());
            responseBody.write(httpResponse.getContentAsByte());
            responseBody.flush();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void hasOnlyOnePathSegmentOrThrow(String[] pathVariables) {
        if(pathVariables.length > 1) {
            throw new TooManyPathSegmentsException("경로가 잘못되었습니다. 올바른 예) .../tasks/1");
        }
    }

    private Task getTaskIfValidPathVariable(HttpRequest httpRequest) {
        // 올바르지 않은 PathVariable 이 들어온 경우에는 예외를 던진다.
        hasOnlyOnePathSegmentOrThrow(httpRequest.getPathVariables());
        Long taskId = getTaskId(httpRequest.getPathVariables());
        Task task = taskRepository.find(taskId);

        if(task == null) {
            throw new NoSuchTaskException("task id: " + taskId + "는 존재하지 않습니다.");
        }

        return task;
    }

    private HttpResponse handleTask(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            // Task 생성
//            case POST -> {
//                Task task = parseJsonToTask(httpRequest.getBody());
//                long taskId = ++Task.taskSequence;
//                tasks.put(taskId, task);
//                return new HttpResponse(parseTaskToJson(task), 201);
//            }
//            // Task 목록 조회
//            case GET -> {
//                // pathVariable 이 올바르게 입력되었고 존재하는 경우
//                if(httpRequest.hasPathVariable()) {
//                    Task task = getTaskIfValidPathVariable(httpRequest);
//                    return new HttpResponse(parseTaskToJson(task), 200);
//                }
//
//                return new HttpResponse(parseTasksToJson(tasks.values()), 200);
//            }
            // Task 삭제
//            case DELETE -> {
//                if(getTaskIfPresentValidPathVariable(httpRequest)) {
//                    HttpResponseMappingFunction deleteTaskMappingFunction =
//                            task -> {
//                                tasks.remove(task);
//                                return new HttpResponse("task id: " + task.getId() + "가 삭제되었습니다.", 200);
//                            };
//
//                    return getHttpResponseIfValidTaskId(getTaskId(httpRequest.getPathVariables()), deleteTaskMappingFunction);
//                }
//
//                throw new IllegalArgumentException("삭제할 task ID를 입력해야 합니다. ex) .../tasks/1");
//            }

            // TODO: Task 수정, PUT, PATCH

            default -> {
                return new HttpResponse("지원하지 않는 메서드입니다.", 405);
            }
        }
    }

    private Long getTaskId(String[] pathVariables) {
        long taskId;

        try {
            taskId = Long.parseLong(pathVariables[0]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id는 숫자로 입력하셔야 합니다. 올바른 예) .../tasks/1");
        }

        return taskId;
    }

    private String getTitle(String json) {
        String[] keyValue = json.replaceAll("[{}]", "").split(":");
        String key = keyValue[0];
        String value = keyValue[1];

        if("title".equalsIgnoreCase(key)) {
            return value;
        }

        throw new WrongTaskJsonException("잘못된 JSON 형식입니다. 올바른 예) { \"title\": \"과제 제출하기\" }");
    }

    private String parseTaskToJson(long taskId) {
//        return myObjectMapper.keyValueToJson(String.valueOf(taskId), tasks.get(taskId).getTitle());
        return null;
    }

    // TODO: Json 문자열을 Task 객체로 바꾸는 메서드 만들기

//    private String parseTasksToJson(Collection<Task> tasks) {
//        try {
//            return objectMapper.writeValueAsString(tasks);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Tasks 컬렉션 객체를 JSON 문자열로 변환하는데 실패하였습니다.");
//        }
//    }
//
//    private Task parseJsonToTask(String body) {
//        try {
//            return objectMapper.readValue(body, Task.class);
//        } catch (JsonProcessingException e) {
//            throw new WrongTaskJsonException("잘못된 JSON 형식입니다. 올바른 예) { \"title\": \"과제 제출하기\" }");
//        }
//    }
}
