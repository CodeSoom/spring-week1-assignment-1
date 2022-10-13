package com.codesoom.assignment.services;

import com.codesoom.assignment.models.HttpResponse;

import java.io.IOException;

public interface HttpRequestService {
    HttpResponse serviceRequest(Long id, String requestBody) throws IOException;
}
