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
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 등록된 path, method에 맞는 executor를 실행해줍니다
 */
public class HttpRouter {

    private HashMap<HttpRouterKey, RouterExecutable> pathMap = new HashMap<>();

    /**
     * GET method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void get(String pathRegex, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.GET, pathRegex), executor);
    }

    /**
     * POST method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void post(String pathRegex, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.POST, pathRegex), executor);
    }

    /**
     * PUT method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void put(String pathRegex, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PUT, pathRegex), executor);
    }

    /**
     * PATCH method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void patch(String pathRegex, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PATCH, pathRegex), executor);
    }

    /**
     * DELETE method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void delete(String pathRegex, RouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.DELETE, pathRegex), executor);
    }

    /**
     * http request를 분석해서 등록된 규칙과 매칭되는 핸들러를 실행합니다.
     * @param exchange http 요청이 들어왔을 때 전달되는 HttpExchange 객체
     * @throws IOException responseCode, content를 작성할 때 에러가 발생할 수 있습니다
     */
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

        Logger.getGlobal().info(method + " " + path);
        if (!body.isEmpty()) {
            Logger.getGlobal().info(body);
        }

        Iterator<HttpRouterKey> keys = pathMap.keySet().iterator();
        while(keys.hasNext()) {
            HttpRouterKey key = keys.next();

            if (key.equalsMethod(methodType) && key.matchesPath(path)) {
                final RouterExecutable executor = pathMap.get(key);
                final HttpRequest request = new HttpRequest(path, body);

                executor.execute(request, responder);
                return;
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
