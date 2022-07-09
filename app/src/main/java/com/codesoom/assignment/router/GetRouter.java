package com.codesoom.assignment.router;

import com.codesoom.assignment.TaskHttpHandler;
import com.codesoom.assignment.mapper.TaskMapper;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.Parser;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class GetRouter {
    private final TaskService taskService;

    public GetRouter(TaskService taskService) {
        this.taskService = taskService;
    }

    public void handle(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/tasks")) {
            String content = TaskMapper.tasksToJson(taskService.getTasks());
            TaskHttpHandler.sendResponse(exchange, HttpStatus.OK, content.getBytes().length);
            TaskHttpHandler.writeResponseBody(exchange, content);
        }

        if (Parser.isDetailMatches(path)) {
            detailHandle(exchange, path);
        }
    }

    /**
     * 숫자 형식의 id를 가진 GET 요청이 왔을 때 id와 같은 task를 찾아 리턴한다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @param path 수신된 경로
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    private void detailHandle(HttpExchange exchange, String path) throws IOException {
        String[] splitedPath = path.split("/");
        Optional<Task> storedTask = taskService.getTask(Parser.extractId(splitedPath));

        if (storedTask.isPresent()) {
            String content = TaskMapper.taskToJson(storedTask.get());
            TaskHttpHandler.sendResponse(exchange, HttpStatus.OK, content.getBytes().length);
            TaskHttpHandler.writeResponseBody(exchange, content);
            return;
        }

        TaskHttpHandler.sendResponse(exchange, HttpStatus.NOT_FOUND, -1);
    }
}
