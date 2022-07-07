package com.codesoom.assignment.network;

import java.io.IOException;

public class HttpRouterExecutorSpy implements HttpRouterExecutable {

    private HttpRequest request;

    private HttpResponse response;

    private ResponseHandler responseHandler;

    public interface ResponseHandler {
        void execute(HttpResponse response) throws IOException;
    }

    public HttpRouterExecutorSpy(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response) throws IOException {
        this.request = request;
        this.response = response;
        responseHandler.execute(response);
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

}
