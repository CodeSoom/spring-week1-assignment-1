package com.codesoom.assignment.models;

import com.codesoom.assignment.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private final String path;
    private final String method;
    private final String body;
    private final ObjectMapper objectMapper = new ObjectMapper();
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

    public RequestMethod getRequestMethod() {
        switch (method) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            case "PUT":
                return RequestMethod.PUT;
            case "PATCH":
                return RequestMethod.PATCH;
            case "DELETE":
                return RequestMethod.DELETE;
            default:
                throw new AssertionError();
        }
    }

    public RequestContent getRequestContent() throws JsonProcessingException {
        return objectMapper.readValue(body, RequestContent.class);
    }

    public boolean isPathWithId() {
        return idPattern.matcher(path).find();
    }
}
