package com.codesoom.assignment.web;

import com.codesoom.assignment.errors.MethodNotAllowedException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.URI;

public class TaskHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = createHttpRequest(httpExchange);

        HttpResponse httpResponse = httpRequest.getHttpResponse(httpExchange);
        httpResponse.send();
    }

    private HttpRequest createHttpRequest(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String method = httpExchange.getRequestMethod();

        try {
            new HttpRequest(path, method);
        } catch (MethodNotAllowedException error) {
            new HttpResponseBadRequest(httpExchange, error.getMessage()).send();
        }

        return new HttpRequest(path, method);
    }
}
