package com.codesoom.assignment.common.response;

import com.codesoom.assignment.common.request.HttpRequest;
import com.codesoom.assignment.common.util.TodoUtils;
import com.codesoom.assignment.domain.Builder;

public class HttpResponse {
    private final Result result;
    private final int statusCode;
    private final String body;


    private HttpResponse(HttpResponseBuilder httpResponseBuilder) {
        this.statusCode = httpResponseBuilder.statusCode;
        this.body = httpResponseBuilder.body;
        this.result = httpResponseBuilder.result;

    }


    public static HttpResponse fail(int statusCode) {
        return new HttpResponseBuilder().statusCode(statusCode)
                                        .result(Result.FAIL)
                                        .builder();
    }
    public static HttpResponse fail(int statusCode,String errorMessage) {
        return new HttpResponseBuilder().statusCode(statusCode)
                                        .body(errorMessage)
                                        .result(Result.FAIL)
                                        .builder();
    }

    public static HttpResponse success(int statusCode,Object body){
        return new HttpResponseBuilder().statusCode(statusCode)
                                        .body(TodoUtils.transferJsonToString (body))
                                        .result(Result.SUCCESS)
                                        .builder();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Result getResult() {
        return result;
    }

    public String getBody() {
        return body;
    }



    public static class HttpResponseBuilder implements Builder<HttpResponse> {
        private int statusCode;
        private String body;
        private Result result;
        private String message;

        public HttpResponseBuilder() {

        }

        public HttpResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public HttpResponseBuilder body(String body) {
            this.body = body;
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
