package com.codesoom.assignment.handler;


import com.codesoom.assignment.model.Task;
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
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
    private ArrayList<Task> taskList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String DOMAIN_ROOT = "/tasks";
    private Long taskId = 1L;
    private final Pattern pathPatternRegex = Pattern.compile("^/tasks/\\d+$");
    String method = "";
    String path = "";
    String body = "";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        settingRequestMessage(exchange);

        if (isCreateRequest()) {
            createTasksProcess(exchange, body);
        } else if (isGetListRequest()) {
            getTasksProcess(exchange);
        } else if (isGetRequest()) {
            findByIdTask(exchange, path);
        } else if (isUpdateRequest()) {
            updateTaskProcess(exchange, path, body);
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
        body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
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
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
        } finally {
            exchange.close();
        }
    }

    /**
     * 할일을 업데이트한다.
     */
    private void updateTaskProcess(HttpExchange exchange, String path, String body) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Long id = parsePathToTaskId(path);
            Task task = searchTask(id);
            Task updateTask = objectMapper.readValue(body, Task.class);
            Task updatedTask = updateTask(task, updateTask.getTitle());

            String jsonUpdatedTask = objectMapper.writeValueAsString(updatedTask);

            bufferedOutputStream.write(jsonUpdatedTask.getBytes(StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), jsonUpdatedTask.getBytes().length);

        } catch (NoSuchElementException e) {
            e.getStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 기존 할일 내용을 요청받은 할일 내용으로 업데이트한다.
     */
    private Task updateTask(Task findTask, String title) throws NoSuchElementException {
        int findTaskIndex = taskList.indexOf(findTask);
        if (findTaskIndex == -1) {
            throw new NoSuchElementException("해당 할일이 존자하지 않습니다.");
        }

        findTask.setTitle(title);
        taskList.set(findTaskIndex, findTask);

        return findTask;
    }

    /**
     * 아이디에 해당하는 할일을 찾는다.
     */
    private void findByIdTask(HttpExchange exchange, String path) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Long id = parsePathToTaskId(path);
            Task task = searchTask(id);
            String findTask = objectMapper.writeValueAsString(task);

            bufferedOutputStream.write(findTask.getBytes(StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), findTask.getBytes().length);
        } catch (NoSuchElementException e) {
            e.getStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
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
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), jsonTaskList.getBytes().length);
        } catch (IOException e) {
            exchange.close();
            throw e;
        }
    }

    /**
     * 요청 받은 할일을 저장 및 저장된 내용을 응답한다.
     */
    private void createTasksProcess(HttpExchange exchange, String body) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            String task = createTask(body);
            bufferedOutputStream.write(task.getBytes(StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(HttpStatus.CREATED.getCode(), task.getBytes().length);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            exchange.close();
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 할일을 생성한다.
     */
    private String createTask(String body) throws JsonProcessingException {
        String content = "";
        try {
            Task task = objectMapper.readValue(body, Task.class);
            task.setId(taskId);
            taskList.add(task);
            taskId++;
            content = objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw e;
        }
        return content;
    }

}