package com.codesoom.assignment;

public enum HttpStatusCode {

    OK(200, "ok"),
    CREATED(201, "created"),

    BAD_REQUEST(400, "bad request"),
    NOT_FOUND(404, "not found"),
    METHOD_NOT_ALLOWED(405, "method not allowed"),

    INTERNAL_SERVER_ERROR(500, "internal server error");

    private int code;
    private String status;

    HttpStatusCode(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
