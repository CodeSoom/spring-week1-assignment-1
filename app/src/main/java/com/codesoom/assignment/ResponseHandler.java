package com.codesoom.assignment;

import java.io.IOException;
import java.util.List;

import static com.codesoom.assignment.JSONParser.*;

public class ResponseHandler {
    public String handle(String method, String path, List<Task> tasks) throws IOException {
        String content = "";

        // check wrong path
        if (!path.matches("/tasks/*[0-9]*")) {
            return "Wrong URI path";
        }

        Long taskId = extractTaskId(path);

        switch (method) {
            case "GET":
                if (path.equals("/tasks")) {
                    return tasksToJSON(tasks);
                }

                if (taskId != null) {
                    return String.valueOf(
                            tasks.stream()
                                    .filter(task -> taskId == task.getId())
                                    .findFirst()
                    );
                }

                break;

            case "POST":
                return taskToJSON(tasks.get(tasks.size() - 1));

            case "PUT": case "PATCH":
                return "ToDo 제목 수정하기";

            case "DELETE":
                return "ToDo 삭제하기";

            default:
                return "Unknown HTTP method";
        }

        return "Wrong URI path";
    }

    private Long extractTaskId(String path) {
        String[] splitPath = path.split("/");

        try {
            return Long.valueOf(splitPath[splitPath.length - 1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
