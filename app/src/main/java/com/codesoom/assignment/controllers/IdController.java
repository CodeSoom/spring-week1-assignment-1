package com.codesoom.assignment.controllers;

import com.codesoom.assignment.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class IdController extends Controller {
    public void handlePatch(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.PATCH.name());
    }

    public void handleDelete(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.DELETE.name());
    }
}
