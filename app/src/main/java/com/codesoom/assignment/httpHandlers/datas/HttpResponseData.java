package com.codesoom.assignment.httpHandlers.datas;

import com.codesoom.assignment.enums.HttpStatus;

public class HttpResponseData {
    private HttpStatus status;
    private String content;

    public HttpResponseData(HttpStatus status, String content) {
        this.status = status;
        this.content = content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}
