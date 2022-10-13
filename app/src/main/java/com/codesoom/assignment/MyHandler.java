package com.codesoom.assignment;

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

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        final Path path = new Path(exchange.getRequestURI());
        final OutputStream outputStream = exchange.getResponseBody();

        if (!path.isValid()) {
            exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.code, 0);
            writeAndFlushAndClose("", outputStream);
            return;
        }

        final HttpRequestService requestService = resolveHttpRequestService(httpMethod);
        final HttpResponse httpResponse = requestService.serviceRequest(path.getId(), exchange);
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
