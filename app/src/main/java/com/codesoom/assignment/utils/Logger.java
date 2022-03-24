package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.networks.BaseResponse;

import java.net.URI;

public class Logger {

    public void logRequest(String requestMethod,
                           URI requestURI,
                           String requestPath,
                           Task requestBody) {

        System.out.println("=====> REQUEST");
        System.out.println("requestMethod = " + requestMethod);
        System.out.println("requestURI = " + requestURI);
        System.out.println("requestPath = " + requestPath);

        if (requestBody != null) {
            System.out.println("requestBody = " + requestBody);
        }
    }

    public void logResponse(BaseResponse response) {
        System.out.println("=====> RESPONSE");
        System.out.println("response = " + response.toString());
        System.out.println();
    }

}
