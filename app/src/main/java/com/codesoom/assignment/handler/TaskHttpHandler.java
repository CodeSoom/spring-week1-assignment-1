package com.codesoom.assignment.handler;


import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.model.RequestBody;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.vo.HttpMethod;
import com.codesoom.assignment.vo.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class TaskHttpHandler implements HttpHandler {
    private ArrayList<Task> taskList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String DOMAIN_ROOT = "/tasks";
    private Long taskId = 1L;
    private final Pattern pathPatternRegex = Pattern.compile("^/tasks/\\d+$");
    String method = "";
    String path = "";

    @Override
    public void handle(HttpExchange exchange) throws IOException, NoSuchElementException {

        settingRequestMessage(exchange);

        if (isCreateRequest()) {
            createTasksProcess(exchange);
        } else if (isGetListRequest()) {
            getTasksProcess(exchange);
        } else if (isGetRequest()) {
            getTask(exchange, path);
        } else if (isUpdateRequest()) {
            updateTaskProcess(exchange, path);
        } else if (isDeleteRequest()) {
            deleteTask(exchange, path);
        }

    }

    private boolean isDeleteRequest() {
        return HttpMethod.DELETE.name().equals(method) && pathPatternRegex.matcher(path).matches();
    }

    private boolean isUpdateRequest() {
        return (HttpMethod.PATCH.name().equals(method) || HttpMethod.PUT.name().equals(method)) && pathPatternRegex.matcher(path).matches();
    }

    private boolean isGetRequest() {
        return HttpMethod.GET.name().equals(method) && pathPatternRegex.matcher(path).matches();
    }

    private boolean isGetListRequest() {
        return HttpMethod.GET.name().equals(method) && path.equals(DOMAIN_ROOT);
    }

    private boolean isCreateRequest() {
        return HttpMethod.POST.name().equals(method) && path.equals(DOMAIN_ROOT);
    }

    /**
     * 요청메시지를 파싱하여 세팅한다.
     */
    private void settingRequestMessage(HttpExchange exchange) {
        method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        path = uri.getPath();
    }

    /**
     * 할일을 삭제한다.
     */
    private void deleteTask(HttpExchange exchange, String path) throws IOException {
        try {
            Long id = parsePathToTaskId(path);
            Task findTask = searchTask(id);
            taskList.remove(findTask);
            exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getCode(), 0);
            exchange.close();
        } catch (NoSuchElementException e) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
            throw e;
        }
    }

    /**
     * 할일을 업데이트한다.
     */
    private void updateTaskProcess(HttpExchange exchange, String path) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Long id = parsePathToTaskId(path);
            Task task = searchTask(id);
            RequestBody requestBody = new RequestBody(exchange);
            Task updateTask = requestBody.read(Task.class);
            Task updatedTask = updateTask(task, updateTask.getTitle());

            String jsonUpdatedTask = objectMapper.writeValueAsString(updatedTask);

            bufferedOutputStream.write(jsonUpdatedTask.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(jsonUpdatedTask);
        } catch (NoSuchElementException e) {
            e.getStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            throw e;
        }

    }

    private int findTaskElement(Task task) {
        int index = taskList.indexOf(task);
        validateTaskIndex(index);
        return index;
    }

    private void validateTaskIndex(int index) {
        if (index == -1) {
            throw new NoSuchElementException("해당 할일이 존재하지 않습니다.");
        }
    }

    private void updateTaskInList(int index, Task task) {
        taskList.set(index, task);
    }

    private void changeTask(Task findTask, String title, int findTaskIndex) {
        findTask.setTitle(title);
        updateTaskInList(findTaskIndex, findTask);
    }

    /**
     * 기존 할일 내용을 요청받은 할일 내용으로 업데이트한다.
     */
    private Task updateTask(Task findTask, String title) throws NoSuchElementException {
        int findTaskIndex = findTaskElement(findTask);
        changeTask(findTask, title, findTaskIndex);
        return findTask;
    }


    /**
     * 아이디에 해당하는 할일을 응답한다.
     */
    private void getTask(HttpExchange exchange, String path) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Task task = findByIdTask(path);
            String findTask = objectMapper.writeValueAsString(task);

            bufferedOutputStream.write(findTask.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(findTask);
        } catch (NoSuchElementException e) {
            e.getStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 아이디에 해당하는 할일을 찾는다.
     */
    private Task findByIdTask(String path) {
        Long id = parsePathToTaskId(path);
        Task task = searchTask(id);
        return task;
    }

    /**
     * 아이디에 해당하는 할일을 검색한다.
     */
    private Task searchTask(Long id) throws NoSuchElementException {
        Task findTask = taskList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 아이디의 할일이 없습니다."));

        return findTask;
    }

    /**
     * path로 부터 taskId를 파싱한다.
     */
    private static Long parsePathToTaskId(String path) {
        String[] splitUrl = path.split("/");
        Long id = Long.parseLong(splitUrl[splitUrl.length - 1]);
        return id;
    }

    /**
     * 전체 할일리스트를 조회한다.
     */
    private void getTasksProcess(HttpExchange exchange) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            String jsonTaskList = objectMapper.writeValueAsString(taskList);
            bufferedOutputStream.write(jsonTaskList.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(jsonTaskList);
        } catch (IOException e) {
            exchange.close();
            throw e;
        }
    }

    /**
     * 요청 받은 할일을 저장 및 저장된 내용을 응답한다.
     */
    private void createTasksProcess(HttpExchange exchange) throws IOException {
        RequestBody requestBody = new RequestBody(exchange);
        Task createTask = requestBody.read(Task.class);
        String createdTaskJson = createTask(createTask);
        new ResponseSuccess(exchange).send(createdTaskJson, HttpStatus.CREATED.getCode());
    }

    /**
     * 할일을 생성한다.
     */
    private String createTask(Task task) throws JsonProcessingException {
        String content = "";
        try {
            task.setId(taskId);
            taskList.add(task);
            taskId++;
            content = objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            throw e;
        }
        return content;
    }

}
