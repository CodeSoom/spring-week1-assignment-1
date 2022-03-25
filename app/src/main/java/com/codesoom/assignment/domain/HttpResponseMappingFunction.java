package com.codesoom.assignment.domain;

import java.util.function.Function;

@FunctionalInterface
public interface HttpResponseMappingFunction extends Function<Task, HttpResponse> {
    @Override
    HttpResponse apply(Task task);
}
