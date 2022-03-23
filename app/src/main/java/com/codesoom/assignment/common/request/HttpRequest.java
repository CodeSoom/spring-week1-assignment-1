package com.codesoom.assignment.common.request;

import com.codesoom.assignment.common.util.TodoUtils;
import com.codesoom.assignment.domain.Builder;

import java.util.Map;

public class HttpRequest {

    private final String path;
    private final String method;
    private final Map<String,Object> requestBody;

    private HttpRequest(HttpRequestBuilder httpRequestBuilder) {
        this.path = httpRequestBuilder.path;
        this.method = httpRequestBuilder.method;
        this.requestBody = TodoUtils.transferStringToJson(httpRequestBuilder.requestBody);
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public String getTodoId() {
        return path.split("/")[2];
    }


    public static class HttpRequestBuilder implements Builder<HttpRequest> {

        private String path;
        private String method;
        private String requestBody;

        public HttpRequestBuilder(){

        }
        public HttpRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public HttpRequestBuilder method(String method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder requestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        @Override
        public HttpRequest builder() {
            return new HttpRequest(this);
        }
    }
}
