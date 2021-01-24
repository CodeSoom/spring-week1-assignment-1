package com.codesoom.assignment.web;

public class ExpectRequestPattern {
    String regex;
    String method;

    public ExpectRequestPattern(String method, String regex) {
        this.method = method;
        this.regex = regex;
    }

    public boolean isMatched(HttpRequest request) {
        if (!request.method.equals(this.method)) {
            return false;
        }
        return request.path.matches(regex);
    }
}
