package com.codesoom.assignment.models;

public class ResponseDto {

    private int httpStatus;
    private String responseBody;

    public ResponseDto() {
    }

    public ResponseDto(int httpStatus, String responseBody) {
        this.httpStatus = httpStatus;
        this.responseBody = responseBody;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
