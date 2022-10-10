package com.codesoom.assignment.enums;

import lombok.Getter;


@Getter
public enum HttpStatus {
    OK(200),CREATED(201), NOCONTENT(204), NOTFOUND(404), METHODNOTFOUND(405);

    public final int status;


    HttpStatus(int status) {
        this.status = status;
    }
}
