package com.codesoom.assignment;

public class ResponseData {
    private final int statusCode;
    private final String contents;

    public ResponseData(int statusCode, String contents) {
        this.statusCode = statusCode;
        this.contents = contents;
    }

    public int statusCode() {
        return statusCode;
    }

    public String contents() {
        return contents;
    }
}
