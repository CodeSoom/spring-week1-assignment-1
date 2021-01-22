package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MyHandler implements HttpHandler {
    private final Map<String, HttpRequestContextBase> requestContextMap = new HashMap<>();

    public void addRequestContext(String path, HttpRequestContextBase httpRequestContext) {
        requestContextMap.put(path, httpRequestContext);
        System.out.println("Added new context at - " + path);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange);
        printHttpRequest(httpRequest);

        if (httpRequest.isServerHealthCheck()) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.OK, exchange);
            return;
        }

        Optional<HttpRequestContextBase> matchedContext = requestContextMap.entrySet().stream()
                .filter(entry -> httpRequest.getPath().startsWith(entry.getKey())).map(Map.Entry::getValue).findFirst();

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
