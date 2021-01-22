package com.codesoom.assignment;

public enum HttpStatusCode {

    OK(200, "OK"),
    CREATED(201, "Created"),

    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

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
