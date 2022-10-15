package com.codesoom.assignment;

import com.codesoom.assignment.services.DeleteService;
import com.codesoom.assignment.services.EditService;
import com.codesoom.assignment.services.GetService;
import com.codesoom.assignment.services.HttpRequestService;
import com.codesoom.assignment.services.PostService;

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
                return EditService.getInstance();
            case DELETE:
                return DeleteService.getInstance();
            default:
                throw new IllegalArgumentException("" + httpMethod.getName());
        }
    }
}
