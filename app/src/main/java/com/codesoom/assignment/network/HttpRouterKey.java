package com.codesoom.assignment.network;

import java.util.Objects;

/**
 * HttpRouter에 HttpMethod와 path 정규식을 등록해두는 용도로 사용
 * Request가 들어왔을 때 HttpRouterKey와 매칭되는지 여부를 파악해서 핸들러를 실행해 줄 수 있습니다.
 * hashCode를 지원해서 HashMap의 Key로 사용될 수 있습니다.
 */
public class HttpRouterKey {
    private HttpMethod method;
    private String pathRegex;
    private int hashCode;

    public HttpRouterKey(HttpMethod method, String pathRegex) {
        this.method = method;
        this.pathRegex = pathRegex;
        this.hashCode = Objects.hash(method, pathRegex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HttpRouterKey that = (HttpRouterKey) obj;
        return method == that.method && pathRegex == that.pathRegex;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public String getPathRegex() {
        return pathRegex;
    }

    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @param path request로 들어온 path
     * @return 등록된 path 정규식과 매칭 여부 반환
     */
    public boolean matchesPath(String path) {
        if (path == null || this.pathRegex == null) {
            return false;
        }
        return path.matches(pathRegex);
    }

    /**
     * @param method request로 들어온 method
     * @return 등록된 HTTPMethod와 일차 여부 확인
     */
    public boolean equalsMethod(HttpMethod method) {
        if (method == null || this.method == null) {
            return false;
        }
        return method.equals(this.method);
    }
}