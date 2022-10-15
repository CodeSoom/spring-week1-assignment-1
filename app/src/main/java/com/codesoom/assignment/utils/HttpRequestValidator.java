package com.codesoom.assignment.utils;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.exceptions.IllegalHttpRequestBodyException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestMethodException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;

public final class HttpRequestValidator {

    private static final String INVALID_PATH_MESSAGE = "잘못된 경로입니다.\n올바른 URI 예시) .../tasks 또는 .../tasks/5";
    private static final String INVALID_METHOD_NAME_MESSAGE = "옳지 않은 Http Method 입니다.\nGET, POST, PUT, PATCH, DELETE 중에서 선택해 주세요.";
    private static final String MISSING_ID_MESSAGE = "Resource Id가 누락되었습니다.\n올바른 URI 예시) .../tasks/5";
    private static final String MISSING_REQUEST_BODY_MESSAGE = "Request Body가 누락되었습니다.\n올바른 Request Body 예시) {\"title\":\"exercise\"}";
    private static final String NOT_JSON_FORMAT = "Json 형식이 아닙니다.\n올바른 Request Body 예시) {\"title\":\"exercise\"} 와 같은 형식으로 입력해주세요.";

    private HttpRequestValidator() {
    }

    public static void checksPathValid(String path) throws IllegalHttpRequestPathException {
        if (path == null || !path.contains("/")) {
            throw new IllegalHttpRequestPathException(INVALID_PATH_MESSAGE);
        }

        final String[] pathArr = path.split("/");
        if (!(pathArr.length == 2 || pathArr.length == 3)) {
            throw new IllegalHttpRequestPathException(INVALID_PATH_MESSAGE);
        }

        final String resourceName = pathArr[1];
        if (!resourceName.equals("tasks")) {
            throw new IllegalHttpRequestPathException(INVALID_PATH_MESSAGE);
        }

        if (pathArr.length == 2) {
            return;
        }

        final String idPart = pathArr[2];
        if (!StringValidator.isNumberFormat(idPart)) {
            throw new IllegalHttpRequestPathException(INVALID_PATH_MESSAGE);
        }
    }

    public static void checksMethodNameValid(String methodName) throws IllegalHttpRequestMethodException {
        switch (methodName) {
            case "GET":
            case "POST":
            case "PUT":
            case "PATCH":
            case "DELETE":
                return;
            default:
                throw new IllegalHttpRequestMethodException(INVALID_METHOD_NAME_MESSAGE);
        }
    }

    public static void checksRequestBodyMissed(HttpMethod method, String requestBody) throws IllegalHttpRequestBodyException {
        if ((HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method))
                && requestBody.isEmpty()) {
            throw new IllegalHttpRequestBodyException(MISSING_REQUEST_BODY_MESSAGE);
        }
    }

    public static void checksIdMissed(HttpMethod method, Long id) throws IllegalHttpRequestPathException {
        if ((HttpMethod.PUT.equals(method) || HttpMethod.DELETE.equals(method)) && id == null) {
            throw new IllegalHttpRequestPathException(MISSING_ID_MESSAGE);
        }
    }

    public static void checksJsonFormat(String body) throws IllegalHttpRequestBodyException {
        if (!JsonConverter.isJsonFormat(body)) {
            throw new IllegalHttpRequestBodyException(NOT_JSON_FORMAT);
        }
    }
}
