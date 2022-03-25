package com.codesoom.assignment.handlers;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpStatus;
import com.codesoom.assignment.models.ResponseDto;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskDto;
import com.codesoom.assignment.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final TaskService taskService;

    private static final String PATH = "/tasks";
    private static final String PATH_REGEX = "^\\/tasks\\/[0-9]+$";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);

    public TaskHttpHandler() {
        this.taskService = new TaskService();
    }

    /**
     * /tasks/{id} 형식에 맞는 URI라면 true를 반환한다.
     */
    private boolean isMatch(String requestURI) {
        final Matcher matcher = PATH_PATTERN.matcher(requestURI);
        return matcher.matches();
    }

    /**
     *  URI로 요청받은 id를 반환한다.
     */
    private Long getPathVariable(String requestURI) {
        String[] splitPaths = requestURI.split("/");
        return Long.valueOf(splitPaths[2]);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().getPath();
        String requestBody = getRequestBody(exchange.getRequestBody());

        System.out.println(requestMethod + " " + exchange.getRequestURI());

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "application/json");

        ResponseDto responseDto = handleRequest(requestMethod, requestURI, requestBody);

        OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(responseDto.getHttpStatus(), responseDto.getResponseBody()
                .getBytes(StandardCharsets.UTF_8).length);

        outputStream.write(responseDto.getResponseBody().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private ResponseDto handleRequest(String httpMethod, String requestURI, String requestBody) throws IOException {
        if (PATH.equals(requestURI) && HttpMethod.GET.name().equals(httpMethod)) {
            return getTasks();
        }
        if (PATH.equals(requestURI) && HttpMethod.POST.name().equals(httpMethod)) {
            return createTask(toTask(requestBody));
        }
        if (isMatch(requestURI) && HttpMethod.GET.name().equals(httpMethod)) {
            return getTask(getPathVariable(requestURI));
        }
        if (isMatch(requestURI) && HttpMethod.PATCH.name().equals(httpMethod)) {
            return updateTask(getPathVariable(requestURI), toTask(requestBody));
        }
        if (isMatch(requestURI) && HttpMethod.DELETE.name().equals(httpMethod)) {
            return deleteTask(getPathVariable(requestURI));
        }

        return handleError(HttpStatus.NOT_FOUND.value(), "Not Found");
    }

    private ResponseDto getTasks() throws IOException {
        return new ResponseDto(HttpStatus.OK.value(), tasksToJSON(taskService.getTasks()));
    }

    private ResponseDto createTask(TaskDto requestDto) throws IOException {
        final Task newTask = taskService.addTask(requestDto);
        return new ResponseDto(HttpStatus.CREATED.value(), taskToJSON(newTask));
    }

    private ResponseDto getTask(Long id) throws IOException {
        final Task findTask = taskService.findTaskById(id);
        return new ResponseDto(HttpStatus.OK.value(), taskToJSON(findTask));
    }

    private ResponseDto updateTask(Long id, TaskDto requestDto) throws IOException {
        final Task updatedTask = taskService.updateTaskById(id, requestDto);
        return new ResponseDto(HttpStatus.OK.value(), taskToJSON(updatedTask));
    }

    private ResponseDto deleteTask(Long id) {
        taskService.deleteTaskById(id);
        return new ResponseDto(HttpStatus.OK.value(), "");
    }

    private ResponseDto handleError(int httpStatus, String message) {
        return new ResponseDto(httpStatus, message);
    }

    /**
     * 모든 Task 목록을 JSON 형식으로 반환한다.
     */
    private String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    /**
     * 하나의 Task 객체를 JSON 형식으로 반환한다.
     */
    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    /**
     * 요청받은 할 일을 Task 객체로 변환하여 반환한다.
     */
    private TaskDto toTask(String content) {
        Pattern pattern = Pattern.compile("\\{\\s*\"title\"\\s*:\\s*\"(.*?)\"\\s*}");
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return new TaskDto(matcher.group(1));
        } else {
            throw new IllegalArgumentException("JSON 형식이 아닙니다.");
        }
    }

    /**
     * 요청받은 JSON 형식의 content를 문자열로 반환한다.
     */
    private String getRequestBody(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

}
