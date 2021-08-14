package com.codesoom.assignment.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private final String path;
    private final String method;
    private final String body;

    private final Pattern idPattern;
    private static final String URL_ID_REGEX = "\\d+";

    public Request(String path, String method, String body) {
        this.path = path;
        this.method = method;
        this.body = body;

        idPattern = Pattern.compile(URL_ID_REGEX);
    }

    public Long getPathId() {
        Matcher idMatcher = idPattern.matcher(path);
        idMatcher.find();

        return Long.parseLong(idMatcher.group(0));
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public boolean isPathWithId() {
        return idPattern.matcher(path).find();
    }
}
