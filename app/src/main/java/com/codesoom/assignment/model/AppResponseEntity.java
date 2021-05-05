package com.codesoom.assignment.model;

public class AppResponseEntity {
    private int httpStatusCode;
    private String contents;

    public AppResponseEntity(int httpStatusCode, String contents) {
        this.httpStatusCode = httpStatusCode;
        this.contents = contents;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "AppResponseEntity{" +
                "httpStatusCode=" + httpStatusCode +
                ", contents='" + contents + '\'' +
                '}';
    }
}
