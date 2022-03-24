package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Tasks;

import java.util.stream.Stream;
import java.util.function.BiFunction;

public enum HttpMethod {
    POST("POST", (resource, tasks) -> {
        String content = "";
        String bodyContent = resource.getContent();
        String path = resource.getPath();
        if(!bodyContent.equals("") && (path.equals("/tasks") || path.equals("/tasks/"))) {
            Task task = new Task(tasks.getSize() + 1L, bodyContent);
            tasks.add(task);

            content = tasks.getLatestTaskToJSON();
        }

        return content;
    }),
    GET("GET", (resource, tasks) -> {
        String content = "";
        String path = resource.getPath();
        if(path.equals("/tasks") || path.equals("/tasks/")) {
            content = tasks.getAlltasksToJSON();
        }

        if(path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                content = tasks.getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        if(path.equals("/")) {
            content = "Hello World!";
        }

        return content;
    }),
    PUT("PUT", (resource, tasks) -> {
        String content = "";
        String bodyContent = resource.getContent();
        String path = resource.getPath();
        if(!bodyContent.equals("")
                && path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                Task task = new Task(number + 1L, bodyContent);
                if(number >= tasks.getSize()) {
                    return "해당 id는 tasks에 없습니다.";
                }
                tasks.set(number, task);

                content = tasks.getTaskToJSON(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        return content;
    }),
    DELETE("DELETE", (resource, tasks) -> {
        String content = "";
        String path = resource.getPath();
        if(path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                if(number >= tasks.getSize()) {
                    return "해당 id는 tasks에 없습니다.";
                }
                tasks.remove(number);
            } catch (NumberFormatException e) {
                content = "id의 숫자를 입력해주세요.";
            }
        }

        return content;
    });

    private String method;
    private BiFunction<Resource, Tasks, String> operation;

    HttpMethod(String method, BiFunction<Resource, Tasks, String> operation) {
        this.method = method;
        this.operation = operation;
    }

    public static HttpMethod findByHttpMethod(String method) {
        return Stream.of(values())
                .filter(httpMethod -> httpMethod.getMethod().equals(method))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Allow: POST, GET, PUT, DELETE"));
    }

    public String operate(Resource resource, Tasks tasks){
        return operation.apply(resource, tasks);
    }

    public String getMethod() {
        return method;
    }
}
