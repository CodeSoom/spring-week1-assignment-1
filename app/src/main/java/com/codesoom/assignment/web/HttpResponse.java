package com.codesoom.assignment.web;

public class HttpResponse {
    int statusCode;
    String content;
    public HttpResponse(int statusCode, String content){
        this.content = content;
        this.statusCode = statusCode;
    }
}
