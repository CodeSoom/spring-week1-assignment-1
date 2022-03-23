package com.codesoom.assignment.utils;

import java.net.URI;

public class Logger {

    public void logRequest(String requestMethod,
                           URI requestURI,
                           String requestPath,
                           String requestBody) {

        System.out.println("=====> REQUEST");
        System.out.println("requestMethod = " + requestMethod);
        System.out.println("requestURI = " + requestURI);
        System.out.println("requestPath = " + requestPath);

        if (requestBody != null && !requestBody.isEmpty()) {
            System.out.println("requestBody = " + requestBody);
        }
    }

    public void logResponse(String responseContent) {
        System.out.println("=====> RESPONSE");
        System.out.println("responseContent = " + responseContent);
        System.out.println();
    }

}
