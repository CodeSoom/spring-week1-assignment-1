package com.codesoom.assignment.infrastructure.mapping;

import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.response.HttpResponse;
import com.codesoom.assignment.domain.mapping.HttpMapping;
import com.codesoom.assignment.domain.todo.TodoService;

public class GetHttpMapping implements HttpMapping {
    private final TodoService todoService;

    public GetHttpMapping(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        return null;
    }
}
