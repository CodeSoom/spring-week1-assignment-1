package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Http 요청에 관련된 정보
 */
public class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String body;

    /**
     * HttpExchange로 부터 Request 정보를 추출
     * @param exchange HttpExchange 객체
     */
    public HttpRequest(HttpExchange exchange) {
        this.method = HttpMethod.convert(exchange.getRequestMethod());
        this.path = exchange.getRequestURI().getPath();
        this.body = getRequestBody(exchange).orElse("");
    }

    /**
     * @return Http Request 정보가 정상적인지 여부 반환
     */
    public boolean isNotValid() {
        return method == null || path == null;
    }

    /**
     * @return HttpMethod enum 반환
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @return request path 반환
     */
    public String getPath() {
        return path;
    }

    /**
     * @return request body 내용 반환. 내용 없는 경우 공백 문자가 반환됩니다.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param exchange HttpExchange
     * @return exchange로 부터 request body를 추출해서 반환
     */
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
