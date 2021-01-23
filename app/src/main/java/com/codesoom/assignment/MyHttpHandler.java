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
    private Long notFoundId = 0L;
    private int minimunMethodLength = 1;

    private

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpRequest httpRequest = new HttpRequest(exchange);
        String content = "Hello, world!";

        if (httpRequest.hasPath().isEmpty()) {
            response(HTTP_NOT_FOUND, content, exchange);
        }

        if (httpRequest.hasMethod().length() >= minimunMethodLength) {
            switch (httpRequest.hasMethod()) {
                case "GET":
                    /* /tasks만 입력했을 경우 (할 일 전체 목록을 얻는 경우) */
                    if (httpRequest.hasPath().equals("/tasks")) {
                        getAllTaskList();
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    /* /tasks/{id}를 입력했을 경우 (입력한 아이디에 해당하는 할 일만 가져오는 경우) */
                    Long id = httpRequest.getId();
                    content = jsonConverter.taskToJson(findTaskById(id));
                    response(HTTP_OK, content, exchange);
                    break;
                case "POST":
                    content = postCreateNewTask(httpRequest);
                    response(HTTP_CREATED, content, exchange);
                    break;
                case "PUT":
                case "PATH":
                    if (putUpdateTaskTitle(httpRequest)) {
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    response(HTTP_NOT_FOUND, content, exchange);
                    break;
                case "DELETE":
                    if (deleteTask(httpRequest.getId())) {
                        content = jsonConverter.tasksToJson(taskRepository.getTaskStore());
                        response(HTTP_OK, content, exchange);
                        break;
                    }
                    if (httpRequest.getId().equals(notFoundId)) {
                        response(HTTP_NOT_FOUND, content, exchange);
                        break;
                    }
                default:
                    System.out.println("default : " + httpRequest.toString()); // 확인용
                    break;
            }
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

    private String postCreateNewTask(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.hasPath();

        if (path.contains("/tasks")) {
            Task newTask = jsonConverter.jsonToTask(httpRequest.hasBody());
            taskRepository.createNewTask(newTask);

            String content = jsonConverter.tasksToJson(taskRepository.getTaskStore()); // content ==> outputStream.toString()을 return한 것
            return content;
        }
        return "POSTCreateNewTask() : content 없음";
    }

    private boolean deleteTask(Long deleteId) throws IOException {
        if (taskRepository.getTaskStore().containsKey(deleteId)) {
            taskRepository.deleteTask(deleteId);
            return true;
        }
        return false;
    }

    private boolean putUpdateTaskTitle(HttpRequest httpRequest) throws IOException {
        Long idForTaskUpdate = httpRequest.getId();
        if (taskRepository.getTaskStore().containsKey(idForTaskUpdate)) {
            Task updateTask = jsonConverter.jsonToTask(httpRequest.hasBody());
            updateTask.setId(idForTaskUpdate);
            taskRepository.updateTaskTitle(idForTaskUpdate, updateTask);
            return true;
        }
        return false;
    }

    private List<Task> getAllTaskList() {
        return taskRepository.findAll();
    }

    private Task findTaskById(Long id) {
        return taskRepository.getTaskById(id);
    }
}
