package com.codesoom.assignment.models;

import com.codesoom.assignment.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http 요청 정보
 */
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

    /**
     * 요청한 URI 에서 id 값을 찾아서 리턴합니다.
     * @return path 내의 id 값
     */
    public Long getPathId() {
        Matcher idMatcher = idPattern.matcher(path);
        idMatcher.find();

        return Long.parseLong(idMatcher.group(0));
    }

    /**
     * String 으로 저장된 메소드를 {@link RequestMethod}으로 변환시켜 리턴합니다.
     * @return {@link RequestMethod}
     * @throws AssertionError   요청 메소드가 RequestMethod 에 없는 경우
     */
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

    /**
     * String 으로 저장된 메소드를 RequestMethod 로 변환시켜 리턴합니다.
     * @return {@link RequestContent}
     * @throws JsonProcessingException  Json 에서 RequestContent 로 변환시 에러가 있는 경우
     */
    public RequestContent getRequestContent() throws JsonProcessingException {
        return objectMapper.readValue(body, RequestContent.class);
    }

    /**
     * path 내에 id 존재 여부를 리턴합니다.
     * @return path 내의 id 존재 여부
     */
    public boolean isPathWithId() {
        return idPattern.matcher(path).find();
    }
}
