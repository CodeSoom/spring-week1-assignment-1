package com.codesoom.assignment.domain.http;

import com.codesoom.assignment.domain.Task;

import java.util.function.Function;

@FunctionalInterface
public interface HttpResponseMappingFunction extends Function<Task, HttpResponse> {
    @Override
    HttpResponse apply(Task task);
}
