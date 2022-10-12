package com.codesoom.assignment;

public class Validator {

    public static boolean isValid(String path, HttpMethod httpMethod) {
        return path != null && httpMethod != null;
    }
}
