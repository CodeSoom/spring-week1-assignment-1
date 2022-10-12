package com.codesoom.assignment.utils;

import com.codesoom.assignment.HttpMethod;

public class Validator {

    public static boolean isValid(String path, HttpMethod httpMethod) {
        return path != null && httpMethod != null;
    }
}
