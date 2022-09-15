package com.codesoom.assignment.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum HttpStatus {
    OK(200),CREATED(201), NOCONTENT(204), NOTFOUND(404);

    public final int status;


}
