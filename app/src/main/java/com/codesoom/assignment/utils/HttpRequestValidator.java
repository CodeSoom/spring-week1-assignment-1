package com.codesoom.assignment.utils;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.exceptions.IllegalHttpRequestBodyException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestMethodException;
import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import com.codesoom.assignment.models.HttpRequest;

public final class HttpRequestValidator {

    private static final String INVALID_PATH_MESSAGE = "잘못된 경로입니다.\n.../tasks/{Number} 형식으로 입력해주세요.";
    private static final String INVALID_METHOD_NAME_MESSAGE = "옳지 않은 Http Method 입니다.\nGET, POST, PUT, PATCH, DELETE 중에서 선택해 주세요.";
    private static final String MISSING_ID_MESSAGE = "Resource Id가 누락되었습니다.\n.../tasks/{Number} 형식으로 입력해주세요.";
    private static final String MISSING_REQUEST_BODY_MESSAGE = "Request Body가 누락되었습니다.\nex) {\"title\":\"Lorem Ipsum\"}";

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
        if (!StringValidator.isNumber(idPart)) {
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

    public static void checksMissingPartExists(HttpRequest httpRequest) throws IllegalHttpRequestException {
        final HttpMethod method = httpRequest.getHttpMethod();
        final Long id = httpRequest.getPath().getId();
        final String requestBody = httpRequest.getRequestBody();

        checksIdMissed(method, id);
        checksRequestBodyMissed(method, requestBody);
    }

    private static void checksRequestBodyMissed(HttpMethod method, String requestBody) throws IllegalHttpRequestBodyException {
        if ((HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method))
                && requestBody.isEmpty()) {
            throw new IllegalHttpRequestBodyException(MISSING_REQUEST_BODY_MESSAGE);
        }
    }

    private static void checksIdMissed(HttpMethod method, Long id) throws IllegalHttpRequestPathException {
        if ((HttpMethod.PUT.equals(method) || HttpMethod.DELETE.equals(method)) && id == null) {
            throw new IllegalHttpRequestPathException(MISSING_ID_MESSAGE);
        }
    }
}
