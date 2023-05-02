package com.codesoom.assignment.handler;

import java.util.Arrays;

public enum TaskApiRoute {
    GET_ONE("GET", "/tasks/", TaskHttpHandler::handleGetOneMethod),
    GET_ALL("GET", "/tasks", TaskHttpHandler::handleGetAllMethod),
    POST("POST", "/tasks", TaskHttpHandler::handlePostMethod),
    PUT("PUT", "/tasks/", TaskHttpHandler::handlePutMethod),
    DELETE("DELETE", "/tasks/", TaskHttpHandler::handleDeleteMethod),
    PATH_NOT_FOUND("", "", TaskHttpHandler::handlePathNotFound);

    private final String method;
    private final String path;
    private final ApiRouteHandler handlerMethod;

    TaskApiRoute(final String method, final String path, final ApiRouteHandler apiRouteHandler) {
        this.method = method;
        this.path = path;
        this.handlerMethod = apiRouteHandler;
    }

    public ApiRouteHandler getHandlerMethod() {
        return handlerMethod;
    }

    public static TaskApiRoute matchRoute(final String method, final String path) {
        return Arrays.stream(TaskApiRoute.values())
                .filter(endpoint -> (endpoint.method.equals(method)) && (path.matches(endpoint.getPathPattern())))
                .findFirst()
                .orElse(PATH_NOT_FOUND);
    }

    private String getPathPattern() {
        if ((this == GET_ONE) || (this == PUT) || (this == DELETE)) {
            return path + "\\d+";
        }
        return path;
    }

}
