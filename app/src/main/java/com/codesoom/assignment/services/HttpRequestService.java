package com.codesoom.assignment.services;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface HttpRequestService {
    String serviceRequest(Long id, HttpExchange exchange) throws IOException;
}
