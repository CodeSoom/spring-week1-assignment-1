package com.codesoom.assignment.infrastructure.mapping;

import com.codesoom.assignment.common.logger.Log;
import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.response.HttpResponse;
import com.codesoom.assignment.common.response.HttpResponseCode;
import com.codesoom.assignment.domain.mapping.HttpMapping;
import com.codesoom.assignment.domain.todo.Todo;
import com.codesoom.assignment.domain.todo.TodoService;

import java.util.logging.Logger;

public class PostHttpMapping implements HttpMapping {
    private final TodoService todoService;
    private final Logger logger = Log.getInstance().getLog(PostHttpMapping.class);

    public PostHttpMapping(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        String title;
        String path = httpRequest.getPath();
        if (path.equals("/tasks")) {
            title = (String) httpRequest.getRequestBody().getOrDefault("title", "");
            return save(title);
        }
        return HttpResponse.fail(HttpResponseCode.NOT_FOUND.getStatusCode(), HttpResponseCode.NOT_FOUND.getStatusDesc());
    }

    private HttpResponse save(String title) {
        Todo todo=null;
        try {
            todo = new Todo.TodoBuilder()
                    .title(title)
                    .builder();
        } catch (IllegalArgumentException e) {
            return HttpResponse.fail(HttpResponseCode.BAD_REQUEST.getStatusCode(),e.getMessage());
        }

        todo = todoService.save(todo);
        return HttpResponse.success(HttpResponseCode.CREATED.getStatusCode(), HttpResponseCode.CREATED.getStatusDesc(), todo);
    }
}
