package com.codesoom.assignment.common.response;

import com.codesoom.assignment.domain.Builder;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {
    private final Result result;
    private final int statusCode;
    private final String message;
    private final T data;

    private HttpResponse(HttpResponseBuilder httpResponseBuilder) {
        this.statusCode = httpResponseBuilder.statusCode;
        this.data = (T) httpResponseBuilder.data;
        this.result = httpResponseBuilder.result;
        this.message = httpResponseBuilder.message;

    }

    public static HttpResponse fail(int statusCode) {
        return new HttpResponseBuilder().statusCode(statusCode)
                .result(Result.FAIL)
                .builder();
    }

    public static HttpResponse fail(int statusCode, String errorMessage) {
        return new HttpResponseBuilder().statusCode(statusCode)
                .message(errorMessage)
                .result(Result.FAIL)
                .builder();
    }

    public static HttpResponse success(int statusCode, Object data) {
        return new HttpResponseBuilder().statusCode(statusCode)
                .data(data)
                .result(Result.SUCCESS)
                .builder();
    }

    public static HttpResponse success(int statusCode, String message, Object data) {
        return new HttpResponseBuilder().statusCode(statusCode)
                .data(data)
                .message(message)
                .result(Result.SUCCESS)
                .builder();
    }


    public int getStatusCode() {
        return statusCode;
    }

    public Result getResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class HttpResponseBuilder implements Builder<HttpResponse> {
        private int statusCode;
        private Object data;
        private Result result;
        private String message;

        public HttpResponseBuilder() {

        }

        public HttpResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public HttpResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public HttpResponseBuilder result(Result result) {
            this.result = result;
            return this;
        }

        public HttpResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public HttpResponse builder() {
            return new HttpResponse(this);
        }
    }


    public enum Result {
        SUCCESS, FAIL
    }
}
