package com.codesoom.assignment;

import com.codesoom.assignment.exception.NotFoundTaskIdException;
import com.codesoom.assignment.model.RequestProcessEntity;
import com.codesoom.assignment.model.HttpMethod;
import com.codesoom.assignment.model.AppResponseEntity;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.model.HttpStatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.URI;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AppHttpHandler implements HttpHandler {
    private final static Logger logger = Logger.getGlobal();
    private final String taskPath = "tasks";
    private List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AppHttpHandler() {
        logger.setLevel(Level.INFO);
    }

    /**
     *  Http Request URL에 대한 요청 처리 후 HTTP 응답을 보낸다.
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        final int taskIdIndex = 2;
        String uriPath = uri.getPath();
        String[] uriPaths = uriPath.split("/");
        String rootPath = uriPaths[1];
        String taskId = "";
        String contents = "";
        if (uriPaths.length > taskIdIndex) {
            taskId = uriPaths[taskIdIndex];
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        logger.info("method : " + method + " /  uriPath : " + uriPath + " / body : " + body);

        HttpStatusCode httpStatusCode = HttpStatusCode.OK;
        try {
            if (taskPath.equals(rootPath)) {
                RequestProcessEntity requestProcessEntity = new RequestProcessEntity
                        .RequestProcessEntityBuilder()
                        .setBody(body)
                        .setMethod(method)
                        .setTaskId(taskId).build();
                AppResponseEntity appResponseEntity = requestProcessor(requestProcessEntity);
                contents = appResponseEntity.getContents();
                httpStatusCode = appResponseEntity.getHttpStatusCode();
            }
        } catch (NotFoundTaskIdException ne) {
            httpStatusCode = HttpStatusCode.NOT_FOUND;
        }


        exchange.sendResponseHeaders(httpStatusCode.getStatusCode(), contents.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(contents.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * HTTP 요청에 대한 HttpStatusCode 값과  ResponseBody 리턴한다.
     * @param requestProcessEntity Task 처리에 관한 HTTP 요청
     * @throws IOException
     */
    private AppResponseEntity requestProcessor(RequestProcessEntity requestProcessEntity) throws IOException {
        String contents = "";
        HttpStatusCode httpStatusCode = HttpStatusCode.OK;
        String taskId = requestProcessEntity.getTaskId();
        String body = requestProcessEntity.getBody();
        String method = requestProcessEntity.getMethod();
        boolean isBody = requestProcessEntity.isBodyBlank();
        HttpMethod httpMethod = HttpMethod.valueOf(method);

        switch (httpMethod) {
            case GET:
                contents = tasksToJSONByPath(taskId);
                httpStatusCode = HttpStatusCode.OK;
                break;
            case POST:
                if (isBody) {
                    break;
                }
                Task task = toTask(body);
                task.setId(getTaskMaxId());
                tasks.add(task);
                httpStatusCode = HttpStatusCode.CREATED;
                contents = tasksToJSON(task);
                break;
            case DELETE:
                deleteTask(taskId);
                httpStatusCode = HttpStatusCode.NO_CONTENT;
                break;
            case PUT:
                contents = modifyTask(requestProcessEntity);
                httpStatusCode = HttpStatusCode.OK;
                break;
            default:
                break;
        }
        return new AppResponseEntity(httpStatusCode, contents);
    }

    /**
     *  요청한 Task ID에  해당 하는  Task를 수정 하고, JSON 문자열로 리턴합니다.
     * @param requestProcessEntity 수정할 Task 요청 정보
     * @return String  JSON 형식의 수정한 Task 정보
     * @throws IOException
     */

    private String modifyTask(RequestProcessEntity requestProcessEntity) throws IOException {
        String contents = "";
        String taskId = requestProcessEntity.getTaskId();
        String body = requestProcessEntity.getBody();
        if ("".equals(taskId)) {
            return " ";
        }
        Task findTask = findTask(taskId);
        int tasksIdx = tasks.indexOf(findTask);
        Task task = toTask(body);
        task.setId(findTask.getId());
        tasks.set(tasksIdx, task);
        contents = tasksToJSON(task);
        return contents;
    }

    /**
     * 요청 Task ID를 가자고 Task를 찾는다.
     * 만약 Task ID가 없다면 Exception Throw  한다.
     * @param taskId  Task 정보를 요청
     * @return Task  Task Class 형식의 Task 정보
     * @Exception NotFoundTaskIdException : Task ID에 해당하는 Task가 없을 경우에 Throw
     */
    private Task findTask(String taskId) {
        Long longTaskId = Long.parseLong(taskId);
        return tasks.stream()
                .filter(t -> t.getId() == longTaskId)
                .findFirst()
                .orElseThrow(NotFoundTaskIdException::new);
    }

    /**
     * 요청한 Task ID에 해당하는 Task를 삭제한다.
     * @param taskId 삭제할 Task 정보
     */
    private void deleteTask(String taskId) {
        if ("".equals(taskId)) {
            return;
        }
        Task task = findTask(taskId);
        tasks.remove(task);
    }

    /**
     * 요청한 Task ID에 해당하는 Task를 찾은 이후 JSON 변환 한다.
     * 만약 Task ID가  없다면 저장된 모든 Task를 JSONArray로 변환 힌다.
     * @param taskId : JSON 변환을 요청한 Task ID
     * @return String :  요청한 Task ID의 Task정보 또는 저장된 모든 Task 정보
     * @throws IOException
     */
    private String tasksToJSONByPath(String taskId) throws IOException {
        String contents = "";
        if (!"".equals(taskId)) {
            Task findTask = findTask(taskId);
            contents = tasksToJSON(findTask);
        } else {
            contents = tasksToJSON();
        }
        return contents;
    }

    /**
     * 응답 내용을 Task 형식의  JSON으로 변환한다.
     * @param contents  JSON 변환을 요청한 정보
     * @return Task  Task Class 형식으로 변환한 정보
     * @throws JsonProcessingException
     */
    private Task toTask(String contents) throws JsonProcessingException {
        return objectMapper.readValue(contents, Task.class);
    }

    /**
     * 전체 Task를 JSON으로 변환
     * @return String 저장된 모든 Task에 대한 JSON 정보
     * @throws IOException
     */
    private String tasksToJSON() throws IOException {
        return tasksToJSON(tasks);
    }

    /**
     * 다수의 Task 정보를 JSON Array String으로 변환한다.
     * @param tasks JSON 형식으로 변환할 Task 정보
     * @return String Task JSON정보
     * @throws IOException
     */
    private String tasksToJSON(Object tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    /**
     * Task ID 최대값을 리턴 한다.
     * @return Long Task ID의 최대값
     */
    private Long getTaskMaxId() {
        Long maxId = 1L;
        if (tasks.size() > 0) {
            Comparator<Task> comparator = Comparator.comparingLong(Task::getId);
            maxId = tasks.stream()
                    .max(comparator)
                    .orElseThrow(NotFoundTaskIdException::new)
                    .getId() + 1;
        }
        return maxId;
    }
}
