package com.codesoom.models;

import com.codesoom.HttpEnum.HttpStatusCode;

public class SendResponseData {

    private int httpStatusCode;
    private String response;

    public void init() {
        this.httpStatusCode = HttpStatusCode.NOTFOUND.getStatus();
        this.response = "[]";
    }
    public SendResponseData(int httpStatusCode, String response) {
        this.httpStatusCode = httpStatusCode;
        this.response = response;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
