package com.codesoom.assignment.models;

public enum RCode {
    OK(200), NOTFOUND(404), CREATE(201);

    final int value;

    RCode(int value) {
       this.value = value;
    }

    public int getValue() {
        return value;
    }
}
