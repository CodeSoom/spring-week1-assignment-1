package com.codesoom.assignment.web;

import java.util.Optional;

@FunctionalInterface
public interface HttpController {
    public Optional<HttpResponse> process(HttpRequest request);
}
