package com.codesoom.assignment.model;

public class ResponseData {

    private int statusCode;

    private String content;

    public ResponseData() {
    }

    public ResponseData(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "statusCode=" + statusCode +
                ", content='" + content + '\'' +
                '}';
    }
}
