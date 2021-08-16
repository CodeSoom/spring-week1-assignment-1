package com.codesoom.assignment.web;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum AllowMethods {
    GET, POST, PUT, PATCH, DELETE;

    private static final Map<String, AllowMethods> stringToEnum = Arrays.stream(values())
        .collect(Collectors.toMap(Enum::toString, method -> method));

    public static Optional<AllowMethods> fromString(String method) {
        return Optional.ofNullable(stringToEnum.get(method));
    }

}
