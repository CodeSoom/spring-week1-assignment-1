package com.codesoom.HttpEnum;

public enum HttpStatusCode {

    OK(200),
    NOTFOUND(404),
    CREATED(201),
    BADREQUEST(400);

    private int status;

    HttpStatusCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }


}
