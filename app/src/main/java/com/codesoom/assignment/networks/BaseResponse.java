package com.codesoom.assignment.networks;

import com.codesoom.assignment.enums.HttpStatusCode;

public class BaseResponse<T> {

    private final HttpStatusCode status;

    private final T body;

    public BaseResponse(HttpStatusCode status) {
        this(status, null);
    }

    public BaseResponse(HttpStatusCode status, T body) {
        this.status = status;
        this.body = body;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public String getStatusMessage() {
        return status.getMessage();
    }

    public T getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", body=" + body +
                '}';
    }
}
