package com.codesoom.assignment;

import com.codesoom.assignment.models.JsonPrinter;
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

            content = JsonPrinter.printTask(tasks.getTask(tasks.getSize() - 1));
        }

        return content;
    }),
    GET("GET", (resource, tasks) -> {
        String content = "";
        String path = resource.getPath();
        if(path.equals("/tasks") || path.equals("/tasks/")) {
            content = JsonPrinter.printAllTasks(tasks);
        }

        if(path.contains("/tasks/") && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            try {
                int number = Integer.valueOf(numberString) - 1;
                content = JsonPrinter.printTask(tasks.getTask(number));
            } catch (NumberFormatException e) {
                content = "id에 숫자를 입력하지 않으셨습니다. 그래서 Task의 정보를 찾을 수 없습니다. " +
                        "id에는 숫자만 입력해주세요.";
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
                tasks.set(number, task);

                content = JsonPrinter.printTask(tasks.getTask(number));
            } catch (NumberFormatException e) {
                content = "id에 숫자를 입력하지 않으셨습니다. 그래서 Task의 정보를 수정할 수 없습니다. " +
                        "id에는 숫자만 입력해주세요.";
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
                tasks.remove(number);
            } catch (NumberFormatException e) {
                content = "id에 숫자를 입력하지 않으셨습니다. 그래서 Task를 지울 수 없습니다. " +
                        "id에는 숫자만 입력해주세요.";
            }
        }

        return content;
    }),
    WRONG_METHOD("WRONG_METHOD", (resource, tasks) -> {
        return "해당 메소드는 실행할 수 없습니다. 메소드를 잘못 입력하셨습니다. " +
                "POST, GET, PUT, DELETE 중에서 입력하세요.";
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
                .orElse(WRONG_METHOD);
    }

    public StatusCode findByStatusCode() {
        if(this.isPost()) {
            return StatusCode.CREATED;
        }
        if(this.isWrongMethod()) {
            return StatusCode.METHOD_NOT_ALLOWED;
        }
        return StatusCode.OK;
    }

    public String operate(Resource resource, Tasks tasks) throws IndexOutOfBoundsException {
        return operation.apply(resource, tasks);
    }

    public String getMethod() {
        return method;
    }

    public boolean isPost() {
        return this == HttpMethod.POST;
    }

    private boolean isWrongMethod() {
        return this == HttpMethod.WRONG_METHOD;
    }

}
