package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 등록된 path, method에 맞는 executor를 실행해줍니다
 */
public class HttpRouter {

    HashMap<HttpRouterKey, RouterExecutable> pathMap = new HashMap<>();

    public void get(String path, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.GET, path), executor);
    }

    public void post(String path, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.POST, path), executor);
    }

    public void put(String path, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PUT, path), executor);
    }

    public void patch(String path, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PATCH, path), executor);
    }

    public void delete(String path, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.DELETE, path), executor);
    }

    public void route(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final HttpResponse responder = new HttpResponse(exchange);

        if (method == null) {
            responder.send(400, null);
            return;
        }

        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        if (path == null) {
            responder.send(400, null);
            return;
        }

        final HttpMethod methodType = HttpMethod.convert(method);
        if (methodType == null) {
            responder.send(404, null);
            return;
        }

        final String body = getRequestBody(exchange).orElse("");

        boolean didMatch = false;
        Iterator<HttpRouterKey> keys = pathMap.keySet().iterator();
        while(keys.hasNext() && !didMatch) {
            HttpRouterKey key = keys.next();

            if (key.equalsMethod(methodType) && key.matchesPath(path)) {
                final RouterExecutable executor = pathMap.get(key);
                final HttpRequest request = new HttpRequest(path, body);

                executor.execute(request, responder);
                didMatch = true;
            }
        }
    }

    private Optional<String> getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        if (inputStream == null) {
            return Optional.empty();
        }

        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return Optional.ofNullable(body);
    }

}
