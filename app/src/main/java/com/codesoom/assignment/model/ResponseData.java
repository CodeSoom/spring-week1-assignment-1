package com.codesoom.assignment.model;

import com.codesoom.assignment.util.HttpStatus;

public class ResponseData {

    private HttpStatus httpStatus;

    private String content;

    public ResponseData() {
    }

    public ResponseData(HttpStatus httpStatus, String content) {
        this.httpStatus = httpStatus;
        this.content = content;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return httpStatus.getStatusCode();
    }

    public String getDescription() {
        return httpStatus.getDescription();
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "httpStatus=" + httpStatus +
                ", content='" + content + '\'' +
                '}';
    }
}
