package com.codesoom.assignment.services;

import com.codesoom.assignment.models.HttpResponse;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface HttpRequestService {
    HttpResponse serviceRequest(Long id, HttpExchange exchange) throws IOException;
}
