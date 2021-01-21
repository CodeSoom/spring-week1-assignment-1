package com.codesoom.assignment.web;

@FunctionalInterface
public interface HttpController {
    public HttpResponse process(HttpRequest request) throws Exception;
}
