package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.http.HttpRequest;
import com.codesoom.assignment.domain.http.HttpResponse;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.exception.NoSuchTaskException;
import com.codesoom.assignment.exception.TooManyPathSegmentsException;
import com.codesoom.assignment.exception.WrongJsonException;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.utils.MyObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Collection;
import java.util.Map;

import static com.codesoom.assignment.domain.http.HttpMethod.*;
import static com.codesoom.assignment.domain.http.HttpStatus.*;

public class TaskHttpHandler implements HttpHandler {
    private final MyObjectMapper myObjectMapper = new MyObjectMapper();
    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            response(exchange, handleTask(new HttpRequest(exchange)));
        } catch (TooManyPathSegmentsException | WrongJsonException | IllegalArgumentException e) {
            response(exchange, new HttpResponse(e.getMessage(), BAD_REQUEST));
        } catch (NoSuchTaskException e) {
            response(exchange, new HttpResponse(e.getMessage(), NOT_FOUND));
        } catch (Exception e) {
            response(exchange, new HttpResponse(e.getMessage(), INTERNAL_SERVER_ERROR));
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

    /**
     * Path Segment 가 1개 이상 들어오면 예외를 던진다.
     */
    private void isValidPathSegment(String[] pathVariables) {
        if(pathVariables.length > 1) {
            throw new TooManyPathSegmentsException("경로가 잘못되었습니다. 올바른 예) .../tasks/1");
        }
    }

    private Task getTaskIfValidPathVariable(HttpRequest httpRequest) {
        Long taskId = getTaskId(httpRequest.getPathVariables());
        Task task = taskRepository.find(taskId);

        if(task == null) {
            throw new NoSuchTaskException("task id: " + taskId + "는 존재하지 않습니다.");
        }

        return task;
    }

    private HttpResponse handleTask(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case POST -> {
                return handleCreate(httpRequest);
            }
            case GET -> {
                if(httpRequest.hasPathVariable()) {
                    return handleReadDetail(httpRequest);
                }
                return handleRead();
            }
            case DELETE -> {
                if(httpRequest.hasPathVariable()) {
                    return handleDelete(httpRequest);
                }

                throw new IllegalArgumentException("삭제할 task ID를 입력해야 합니다. ex) .../tasks/1");
            }
            case PUT, PATCH -> {
                if(httpRequest.hasPathVariable()) {
                    return handleUpdate(httpRequest);
                }

                throw new IllegalArgumentException("수정할 task ID를 입력해야 합니다. ex) .../tasks/1");
            }
            default -> {
                return new HttpResponse("지원하지 않는 메서드입니다.", METHOD_NOT_ALLOWED);
            }
        }
    }

    /**
     * 태스크 수정을 처리한다.
     */
    private HttpResponse handleUpdate(HttpRequest httpRequest) {
        Task task = getTaskIfValidPathVariable(httpRequest);
        String updatedTitle = getJsonProperties(httpRequest.getBody()).get("title");
        Task updatedTask = taskRepository.update(task.getId(), updatedTitle);
        return new HttpResponse(getJson(updatedTask), SUCCESS);
    }

    /**
     * 태스크 삭제를 처리한다.
     */
    private HttpResponse handleDelete(HttpRequest httpRequest) {
        Task task = getTaskIfValidPathVariable(httpRequest);
        taskRepository.remove(task.getId());
        return new HttpResponse("task id: " + task.getId() + "가 삭제되었습니다.", SUCCESS);
    }

    /**
     * 태스크 목록 읽기를 처리한다.
     */
    private HttpResponse handleRead() {
        return new HttpResponse(getJsonArray(taskRepository.findAll()), SUCCESS);
    }

    /**
     * 태스트 단건 읽기를 처리한다.
     */
    private HttpResponse handleReadDetail(HttpRequest httpRequest) {
        Task task = getTaskIfValidPathVariable(httpRequest);
        return new HttpResponse(getJson(task), SUCCESS);
    }

    /**
     * 태스크 생성을 처리한다.
     */
    private HttpResponse handleCreate(HttpRequest httpRequest) {
        Task task = getTask(httpRequest.getBody());
        taskRepository.save(task);
        return new HttpResponse(getJson(task), CREATED);
    }

    /**
     * 올바른 PathSegment 가 들어왔다면, TaskId 를 반환한다.
     */
    private Long getTaskId(String[] pathVariables) {
        isValidPathSegment(pathVariables);

        long taskId;

        try {
            taskId = Long.parseLong(pathVariables[0]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id는 숫자로 입력하셔야 합니다. 올바른 예) .../tasks/1");
        }

        return taskId;
    }

    /**
     * Task 객체를 받아서 JSON 문자열을 반환한다.
     */
    private String getJson(Task task) {
        return myObjectMapper.writeAsString(task);
    }

    /**
     * Task 객체의 컬렉션을 받아서 JSON 문자열을 반환한다.
     */
    private String getJsonArray(Collection<Task> tasks) {
        return myObjectMapper.getJsonArray(tasks);
    }

    /**
     * JSON 문자열을 받아서 Task 객체를 반환한다.
     */
    private Task getTask(String json) {
        return myObjectMapper.readValue(json, Task.class);
    }

    /**
     * JSON 문자열을 받아서 각 프로퍼티들을 Map 형태에 매핑한다.
     */
    private Map<String, String> getJsonProperties(String json) {
        return myObjectMapper.getJsonPropertyMap(json);
    }
}
