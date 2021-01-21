package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

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
                    System.out.println("GET : " + httpRequest.toString()); // 확인용
                    // /tasks만 입력했을 경우 (아이디로 검색하지 않고 전체 목록 얻을 때)
                    if (httpRequest.hasPath().equals("/tasks")) {
                        break;
                    }
                    break;
                case "POST":
                    System.out.println("POST : " + httpRequest.toString()); // 확인용
                    content = POSTCreateNewTask(httpRequest);
                    response(200, content, exchange);
                    break;
                case "PUT":
                case "PATH":
                    System.out.println(httpRequest.toString());
                    break;
                case "DELETE":
                    System.out.println("DELETE : " + httpRequest.toString()); // 확인용
                    content = DELETETask(httpRequest);
                    response(200, content, exchange);
                    break;
                default:
                    System.out.println(httpRequest.toString());
                    break;
            }
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

    private boolean getIdFromPath(HttpRequest httpRequest) {
        String[] pathSplit = httpRequest.hasPath().split("/tasks/");
        System.out.println(pathSplit[0]);
        System.out.println(pathSplit[1]);
        int pathWhitId = 2;
        int idIndex = 1;
        if (pathSplit.length == pathWhitId) {
            idInPath = Long.parseLong(pathSplit[idIndex]);
            return true;
        }
        return false;
    }

    private String POSTCreateNewTask(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.hasPath();

        if (path.contains("/tasks")) {
            Task newTask = jsonConverter.JSONToTask(httpRequest.hasBody());
            taskRepository.createNewTask(newTask);
            String content = JSONConverter.tasksToJSON(TaskRepository.getTaskStore()); // content ==> outputStream.toString()을 return한 것
            return content;
        }
        return "POSTCreateNewTask() : content 없음";
    }

    private String DELETETask(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.hasPath();

        if (getIdFromPath(httpRequest)) {
            Long deleteId = idInPath;
            taskRepository.deleteTask(deleteId);
            String content = JSONConverter.tasksToJSON(TaskRepository.getTaskStore());
            System.out.println("content : " + content);
            return content;
        }
        return "DELETETask() : content 없음";
    }

}
