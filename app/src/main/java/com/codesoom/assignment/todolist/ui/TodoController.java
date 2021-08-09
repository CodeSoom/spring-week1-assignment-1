package com.codesoom.assignment.todolist.ui;

import com.codesoom.assignment.todolist.domain.Controller;
import com.codesoom.assignment.http.Response;
import com.codesoom.assignment.todolist.application.TodoService;
import com.codesoom.assignment.todolist.domain.PathVariable;
import com.codesoom.assignment.todolist.domain.RequestMapping;
import com.codesoom.assignment.todolist.domain.Task;
import com.codesoom.assignment.todolist.util.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.net.HttpURLConnection.*;

@RequestMapping(value = "/tasks")
public class TodoController implements Controller {
    public static final String PATH_DELIMITER = "/";
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(method = HttpMethod.GET)
    public Response findTasks(HttpExchange exchange) {
        List<Task> tasks = todoService.findAll();

        return Response.of(HTTP_OK, tasks);
    }

    @RequestMapping(value = "/{id}", method = HttpMethod.GET)
    public Response findOne(HttpExchange exchange, @PathVariable Long id) {
        final Task findTask = todoService.findById(id);

        if (Objects.isNull(findTask)) {
            return Response.from(HTTP_NOT_FOUND);
        }

        return Response.of(HTTP_OK, findTask);

    }

    @RequestMapping(method = HttpMethod.POST)
    public Response saveTask(HttpExchange exchange, Task task) {
        final Task savedTask = todoService.save(task);

        return Response.of(HTTP_CREATED, savedTask);
    }

    @RequestMapping(value = "/{id}", method = HttpMethod.PATCH)
    public Response updateTask(HttpExchange exchange, @PathVariable Long id, Task task) {
        final Task updated = todoService.update(id, task);
        return Response.of(HTTP_OK, updated);
    }

    //TODO RequestMapping의 method attribute를 배열로 받게해서 중복된 해당 컨트롤러 삭제 필요
    @RequestMapping(value = "/{id}", method = HttpMethod.PUT)
    public Response overwriteTask(HttpExchange exchange, @PathVariable Long id, Task task) {
        final Task updated = todoService.update(id, task);
        return Response.of(HTTP_OK, updated);
    }

    @RequestMapping(value = "/{id}", method = HttpMethod.DELETE)
    public Response deleteTask(HttpExchange exchange, @PathVariable Long id) {
        todoService.deleteById(id);

        return Response.from(HTTP_NO_CONTENT);
    }


    @Override
    public boolean support(URI uri) {
        final RequestMapping annotation = this.getClass().getAnnotation(RequestMapping.class);
        final String path = uri.getPath();

        return path.startsWith(annotation.value());
    }

    @Override
    public Method getAvailMethod(HttpExchange exchange) {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> availableMethod(m, exchange))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean availableMethod(Method method, HttpExchange exchange) {
        final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        final String mainPath = this.getClass().getDeclaredAnnotation(RequestMapping.class).value();

        if (Objects.isNull(annotation) || notMatchedHttpMethod(exchange, annotation)) {
            return false;
        }

        final String[] paths = exchange.getRequestURI()
                .getPath().split(PATH_DELIMITER);

        final String[] split = (mainPath + annotation.value()).split(PATH_DELIMITER);


        return paths.length == split.length;
    }

    private boolean notMatchedHttpMethod(HttpExchange exchange, RequestMapping annotation) {
        return !annotation.method().equals(HttpMethod.from(exchange.getRequestMethod()));
    }

}
