package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.services.DeleteService;
import com.codesoom.assignment.services.EditService;
import com.codesoom.assignment.services.GetService;
import com.codesoom.assignment.services.HttpRequestService;
import com.codesoom.assignment.services.PostService;
import com.codesoom.assignment.utils.Validator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final OutputStream outputStream = exchange.getResponseBody();

        if (!Validator.isValid(path, httpMethod)) {
            exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.code, 0);
            writeAndFlushAndClose("", outputStream);
            return;
        }

        final String[] pathArr = path.split("/");

        Long id = null;
        if (pathArr.length == 3) {
            id = Long.valueOf(pathArr[2]);
        }

        final HttpRequestService requestService = resolveHttpRequestService(httpMethod);
        final HttpResponse httpResponse = requestService.serviceRequest(id, exchange);
        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode().code, httpResponse.getContent().getBytes().length);
        writeAndFlushAndClose(httpResponse.getContent(), outputStream);
    }

    private void writeAndFlushAndClose(String content, OutputStream outputStream) throws IOException {
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private HttpRequestService resolveHttpRequestService(HttpMethod httpMethod) {
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
                throw new IllegalArgumentException();
        }
    }
}
