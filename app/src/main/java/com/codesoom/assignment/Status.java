package com.codesoom.assignment;

enum Status {
    OK(200), CREATED(201), NO_CONTENT(204), BAD_REQUEST(400), NOT_FOUND(404);

    private final int status;

    Status(int status) {
        this.status = status;
    }

    int getStatus() {
        return this.status;
    }
}
