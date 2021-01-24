package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MyHandler implements HttpHandler {
    private final List<HttpRequestContextBase> requestContextMap = new ArrayList<>();

    public void addRequestContext(HttpRequestContextBase httpRequestContext) {
        requestContextMap.add(httpRequestContext);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange);
        printHttpRequest(httpRequest);

        if (httpRequest.isServerHealthCheck()) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.OK, exchange);
            return;
        }

        Optional<HttpRequestContextBase> matchedContext =
            requestContextMap.stream().filter(e -> e.matchesPath(httpRequest.getPath())).findFirst();

        if (matchedContext.isEmpty()) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.NOT_FOUND, exchange);
            return;
        }

        matchedContext.get().processHttpRequest(httpRequest, exchange);
    }

    private void printHttpRequest(HttpRequest httpRequest) {
        System.out.println("Received new request - " + httpRequest.toString());
    }
}
