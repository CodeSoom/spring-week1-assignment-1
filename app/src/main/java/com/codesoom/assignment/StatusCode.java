package com.codesoom.assignment;

public enum StatusCode {
    OK(200),
    Created(201);


    private int statusNumber;

    StatusCode(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getStatusNumber() {
        return statusNumber;
    }
}
