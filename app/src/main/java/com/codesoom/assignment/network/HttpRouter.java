package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 등록된 path, method에 맞는 executor를 실행해줍니다
 */
public class HttpRouter {

    private final HashMap<HttpRouterKey, HttpRouterExecutable> pathMap = new HashMap<>();

    /**
     * GET method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void get(String pathRegex, HttpRouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.GET, pathRegex), executor);
    }

    /**
     * POST method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void post(String pathRegex, HttpRouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.POST, pathRegex), executor);
    }

    /**
     * PUT method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void put(String pathRegex, HttpRouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PUT, pathRegex), executor);
    }

    /**
     * PATCH method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void patch(String pathRegex, HttpRouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.PATCH, pathRegex), executor);
    }

    /**
     * DELETE method에 대한 executor를 등록합니다.
     * @param pathRegex 매칭에 활용할 path 정규식
     * @param executor 등록된 method와 path가 일치할 때 executor를 호출합니다
     */
    public void delete(String pathRegex, HttpRouterExecutable executor) {
        pathMap.put(new HttpRouterKey(HttpMethod.DELETE, pathRegex), executor);
    }

    /**
     * http request를 분석해서 등록된 규칙과 매칭되는 핸들러를 실행합니다.
     * @param exchange http 요청이 들어왔을 때 전달되는 HttpExchange 객체
     * @throws IOException responseCode, content를 작성할 때 에러가 발생할 수 있습니다
     */
    public void route(HttpExchange exchange) throws IOException {
        final HttpRequest request = new HttpRequest(exchange);
        final HttpResponse response = new HttpResponse(exchange);

        if (request.isNotValid()) {
            response.send(HttpResponseCode.NotFound, null);
            return;
        }

        for (Map.Entry<HttpRouterKey, HttpRouterExecutable> entry: pathMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                entry.getValue().execute(request, response);
                return;
            }
        }
    }

}