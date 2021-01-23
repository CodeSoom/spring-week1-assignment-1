package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.codesoom.assignment.HttpStatusCode.*;

public class MyHttpHandler implements HttpHandler {

    JSONConverter jsonConverter = new JSONConverter();
    TaskRepository taskRepository = new TaskRepository();
    private Long idInPath;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpRequest httpRequest = new HttpRequest(exchange);
        String content = "Hello, world!";


        if (!httpRequest.hasMethod().isEmpty()) {
            switch (httpRequest.hasMethod()) {
                case "GET":
                    // /tasks만 입력했을 경우 (아이디로 검색하지 않고 전체 목록 얻을 때)
                    if (httpRequest.hasPath().equals("/tasks")) {
                        GETAllTaskList();
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    // tasks/{id}를 입력했을 경우 (입력한 아이디에 해당하는 할일만 보여줌)
                    Long id = getIdFromPath(httpRequest);
                    findOne(id);
                    content = jsonConverter.taskToJson(findOne(id));
                    System.out.println("content2 : " + content);
                    response(HTTP_CREATED, content, exchange);
                    break;
                case "POST":
                    content = POSTCreateNewTask(httpRequest);
                    response(HTTP_CREATED, content, exchange);
                    break;
                case "PUT":
                case "PATH":
                    if (PUTUpdateTaskTitle(httpRequest)) {
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    response(HTTP_NOT_FOUND, content, exchange);
                    break;
                case "DELETE":
                    if (DELETETask(httpRequest)) { // id가 있고 정상적일 때
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    response(HTTP_NOT_FOUND, content, exchange); // id 값이 없을 때
                    break;
                default:
                    System.out.println("default : " + httpRequest.toString()); // 확인용
                    break;
            }
        } else {
            response(HTTP_NOT_FOUND, content, exchange);
        }


        if (httpRequest.hasMethod().isEmpty()) {
            content = "There isn't Method";
            exchange.sendResponseHeaders(404, content.length());
        }

        // response 처리
        OutputStream outputStream = exchange.getResponseBody(); // getResponseBody() : Response를 byte 배열로 반환
        outputStream.write(content.getBytes());  // 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보냄
        outputStream.flush(); // 버퍼에 남아있는 데이터를 모두 출력시키고 버퍼를 비움
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌
    }

    public void response(int responseCode, String content, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(responseCode, content.length());
    }

    private boolean pathExists(HttpRequest httpRequest) {
        String path = httpRequest.hasPath();
        String idInPath = path.replace("/tasks/", "");
        if (idInPath == null) {
            return false;
        }
        return true;
    }

    private Long getIdFromPath(HttpRequest httpRequest) {
        if (pathExists(httpRequest)) {
            String path = httpRequest.hasPath();
            String id = path.replace("/tasks/", "");
            long idInPath = Long.parseLong(id);
            return idInPath;
        }
        return 0L;
    }

    private String POSTCreateNewTask(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.hasPath();

        if (path.contains("/tasks")) {
            Task newTask = jsonConverter.jsonToTask(httpRequest.hasBody());
            taskRepository.createNewTask(newTask);

            String content = jsonConverter.tasksToJson(taskRepository.getTaskStore()); // content ==> outputStream.toString()을 return한 것
            return content;
        }
        return "POSTCreateNewTask() : content 없음";
    }

    private boolean DELETETask(HttpRequest httpRequest) throws IOException {
        Long deleteId = getIdFromPath(httpRequest);

        // path의 id가 storeTaks에 있을 때
        if (pathExists(httpRequest) && taskRepository.getTaskStore().containsKey(deleteId)) {
            taskRepository.deleteTask(deleteId);
            return true;
        }
        return false;
    }

    private boolean PUTUpdateTaskTitle(HttpRequest httpRequest) throws IOException {
        Long idForTaskUpdate = getIdFromPath(httpRequest);

        if (pathExists(httpRequest) && taskRepository.getTaskStore().containsKey(idForTaskUpdate)) {
            Task updateTask = jsonConverter.jsonToTask(httpRequest.hasBody());
            updateTask.setId(idForTaskUpdate);
            taskRepository.updateTaskTitle(idForTaskUpdate, updateTask);
            return true;
        }
        return false;
    }

    private List<Task> GETAllTaskList() {
        return taskRepository.findAll();
    }

    private Task findOne(Long id) {
        return taskRepository.getTaskById(id);
    }
}
