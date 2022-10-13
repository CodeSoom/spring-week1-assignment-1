package com.codesoom.assignment;

import com.codesoom.assignment.exceptions.IllegalHttpRequestException;
import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.services.DeleteService;
import com.codesoom.assignment.services.EditService;
import com.codesoom.assignment.services.GetService;
import com.codesoom.assignment.services.HttpRequestService;
import com.codesoom.assignment.services.PostService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceDispatcher implements HttpHandler {

    private final Map<HttpMethod, HttpRequestService> serviceMap = new ConcurrentHashMap<>();

    public ServiceDispatcher() {
        serviceMap.put(HttpMethod.GET, GetService.getInstance());
        serviceMap.put(HttpMethod.POST, PostService.getInstance());
        serviceMap.put(HttpMethod.PUT, EditService.getInstance());
        serviceMap.put(HttpMethod.PATCH, EditService.getInstance());
        serviceMap.put(HttpMethod.DELETE, DeleteService.getInstance());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();

        HttpRequest httpRequest;
        try {
            httpRequest = new HttpRequest(exchange);
        } catch (IllegalHttpRequestException e) {
            String message = e.getMessage();
            exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.code, message.getBytes().length);
            writeAndFlushAndClose(outputStream, message);
            return;
        }

        final HttpRequestService requestService = serviceMap.get(httpRequest.getHttpMethod());
        final Long id = httpRequest.getPath().getId();
        final String requestBody = httpRequest.getRequestBody();
        final HttpResponse httpResponse = requestService.serviceRequest(id, requestBody);

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode().code, httpResponse.getContent().getBytes().length);
        writeAndFlushAndClose(outputStream, httpResponse.getContent());
    }

    private void writeAndFlushAndClose(OutputStream outputStream, String content) throws IOException {
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
