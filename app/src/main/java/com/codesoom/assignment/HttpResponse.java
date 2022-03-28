package com.codesoom.assignment;

public class HttpResponse {

    private Integer statusCode;
    private String response;

    public HttpResponse(Integer statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
