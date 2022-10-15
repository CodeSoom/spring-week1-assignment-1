package com.codesoom.assignment;

import com.codesoom.assignment.services.HttpRequestService;
import com.codesoom.assignment.services.GetService;
import com.codesoom.assignment.services.PostService;
import com.codesoom.assignment.services.TaskEditService;
import com.codesoom.assignment.services.DeleteService;

public final class HttpRequestServiceResolver {

    private HttpRequestServiceResolver() {
    }

    public static HttpRequestService resolve(HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                return GetService.getInstance();
            case POST:
                return PostService.getInstance();
            case PUT:
            case PATCH:
                return TaskEditService.getInstance();
            case DELETE:
                return DeleteService.getInstance();
            default:
                throw new IllegalArgumentException("" + httpMethod.getName());
        }
    }
}
