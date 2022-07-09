package com.codesoom.assignment.router;

import com.codesoom.assignment.TaskHttpHandler;
import com.codesoom.assignment.mapper.TaskMapper;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.service.Parser;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;

public class PostRouter {
    private final TaskService taskService;

    public PostRouter(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST 요청을 받아 요청 본문이 비었으면 400을 리턴하고
     * 아니라면 Task 객체를 생성하고 201과 함께 리턴합니다.
     *
     * @param exchange 수신된 요청과 응답을 가진 파라미터
     * @throws IOException 입출력에 문제가 있는 경우 던집니다.
     */
    public void handle(HttpExchange exchange) throws IOException {
        String request = Parser.parsingRequest(exchange.getRequestBody());

        if (request.isBlank()) {
            TaskHttpHandler.sendResponse(exchange, HttpStatus.BAD_REQUEST, -1);
            return;
        }

        HashMap requestMap = TaskMapper.getRequestMap(request);
        request = (String) requestMap.get("title");

        String content =  TaskMapper.taskToJson(taskService.createTask(request));

        TaskHttpHandler.sendResponse(exchange, HttpStatus.CREATED, content.getBytes().length);
        TaskHttpHandler.writeResponseBody(exchange, content);
    }
}
