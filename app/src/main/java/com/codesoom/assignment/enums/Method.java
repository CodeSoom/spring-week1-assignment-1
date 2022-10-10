package com.codesoom.assignment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Method {
    GET("GET"), POST("POST") , PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    public final String label;


}
