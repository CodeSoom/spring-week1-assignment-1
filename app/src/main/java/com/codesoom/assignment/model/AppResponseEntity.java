package com.codesoom.assignment.model;

public class AppResponseEntity {
    private HttpStatusCode httpStatusCode;
    private String contents;

    public AppResponseEntity(HttpStatusCode httpStatusCode, String contents) {
        this.httpStatusCode = httpStatusCode;
        this.contents = contents;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "AppResponseEntity{" +
                "httpStatusCode=" + httpStatusCode +
                ", contents='" + contents + '\'' +
                '}';
    }
}
