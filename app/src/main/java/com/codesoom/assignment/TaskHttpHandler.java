package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
    private List<Task> taskList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long curTaskID = 0L;
    private final int SUCCESS = 200;
    private final int CREATED = 201;
    private final int DELETED = 204;
    private final int NOT_FOUND = 404;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(method.equals("GET") && path.equals("/tasks")){
            String content = taskListToJSON();
            exchange.sendResponseHeaders(SUCCESS, content.getBytes().length);
            sendResponseBody(exchange,content);
        }

        if(method.equals("GET") && path.startsWith("/tasks/")){
            String searchIDString = path.substring("/tasks/".length());
            System.out.println("ID String : " + searchIDString);
            getRequest(exchange,searchIDString);
        }

        if(method.equals("POST") && path.equals("/tasks") && !body.isBlank()) {
            postRequest(exchange, body);
        }

        if (method.equals("PUT") && path.startsWith("/tasks/") && !body.isBlank()) {
            String searchIDString = path.substring("/tasks/".length());
            System.out.println("ID String : " + searchIDString);
            putRequest(exchange,searchIDString, body);
        }

        if (method.equals("DELETE") && path.startsWith("/tasks/")) {
            String searchIDString = path.substring("/tasks/".length());
            System.out.println("ID String : " + searchIDString);
            deleteRequest(exchange, searchIDString);
        }
    }

    private Long isValidURL(String IDString)  {
        try {
            Long searchID = Long.parseLong(IDString);
            return searchID;
        }
        catch (Exception e){
            throw e;
        }
    }

    private void sendResponseBody(HttpExchange exchange, String content) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void postRequest(HttpExchange exchange, String body) throws IOException {
        Task task = makeTask(body);
        task.setId(++curTaskID);
        taskList.add(task);

        String content = taskToJSON(task);
        exchange.sendResponseHeaders(CREATED, content.getBytes().length);
        sendResponseBody(exchange, content);
    }

    private void getRequest(HttpExchange exchange, String strID) throws IOException {
        String content = "";
        try{
            Long searchID = Long.parseLong(strID);
            int taskIdx = findTaskIdx(searchID);

            content = taskToJSON(taskList.get(taskIdx));
            exchange.sendResponseHeaders(SUCCESS, content.getBytes().length);
        }
        catch (IndexOutOfBoundsException idxError) {
            System.out.println(idxError);
            content = "ID not exist";
            exchange.sendResponseHeaders(NOT_FOUND, content.getBytes().length);
        }
        catch (Exception e){
            System.out.println("Not Valid URL");
            content = "Not Valid URL";
            exchange.sendResponseHeaders(NOT_FOUND, content.getBytes().length);
        }

        sendResponseBody(exchange,content);
    }

    private void putRequest(HttpExchange exchange, String strID, String body) throws IOException {

        String content = "";
        try {
            int taskIdx = findTaskIdx(isValidURL(strID));

            Task revisedTask = makeTask(body);
            taskList.get(taskIdx).setTitle(revisedTask.getTitle());

            content = taskToJSON(taskList.get(taskIdx));
            exchange.sendResponseHeaders(SUCCESS, content.getBytes().length);

        } catch (IndexOutOfBoundsException idxError) {
            System.out.println(idxError);
            content = "ID not exist";
            exchange.sendResponseHeaders(NOT_FOUND, content.getBytes().length);
        } catch (Exception e) {
            System.out.println("유효한 주소가 아닙니다.");
            content = "Not valid URL";
            exchange.sendResponseHeaders(NOT_FOUND, content.getBytes().length);
        }
        sendResponseBody(exchange,content);
    }

    private void deleteRequest(HttpExchange exchange, String strID) throws IOException {

        String content = new String();
        try{
            int taskIdx = findTaskIdx(isValidURL(strID));
            taskList.remove(taskIdx);
            exchange.sendResponseHeaders(DELETED, -1);
        }catch (Exception e) {
            content = "Not valid URL";
            exchange.sendResponseHeaders(NOT_FOUND, content.getBytes().length);
        }
        sendResponseBody(exchange,content);
    }

    private int findTaskIdx(Long id){
        for(int i=0; i<taskList.size(); i++){
            if(taskList.get(i).getId().equals(id)){
                return i;
            }
        }
        throw new IndexOutOfBoundsException("해당 ID가 존재하지 않습니다.");
    }


    private Task makeTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    /**
     * taskList -> JSON으로 변환 후 return
     *
     * @return JSON
     * @throws IOException JSON으로 변환 불가
     * (Task Class 내에 Getter 미존재)
     */
    private String taskListToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskList);

        return outputStream.toString();
    }

    /**
     * task -> JSON -> JSONString 변환
     * @return JSONString
     * @throws JsonProcessingException JSON파싱 에러
     * */
    private String taskToJSON(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
