package com.codesoom.HttpEnum;

public enum HttpStatusCode {

    OK(200),
    NOTFOUND(404),
    CREATED(201),
    NOCONTENT(204),
    BADREQUEST(400);

    private int status;

    HttpStatusCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }


}
