package com.codesoom.assignment;

import java.io.IOException;
import java.util.List;

import static com.codesoom.assignment.JSONParser.*;

public class ResponseHandler {
    String handle(String method, String path, List<Task> tasks) throws IOException {
        String content = "";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON(tasks);
        } else if (method.equals("GET") && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 상세 조회하기";
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = taskToJSON(tasks.get(tasks.size() - 1));
        }

        if ((method.equals("PUT") || method.equals("PATCH")) && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 제목 수정하기";
        }

        if (method.equals("DELETE") && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 삭제하기";
        }

        return content;
    }
}
