package com.codesoom.assignment;

import com.codesoom.assignment.exceptions.IllegalHttpRequestException;
import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.services.HttpRequestService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class communicates with clients and requests works for processing HTTP message to HttpRequestService.
 * It overrides {@code handle()} which handles HTTP request and sends response back to the client who has requested.
 *
 * @author steve7867
 */
public class ServiceDispatcher implements HttpHandler {
    /**
     * This method communicates with clients using HttpExchange.
     *
     * @param exchange the exchange containing the request from the
     *      client and used to send the response
     * @throws IOException  if Writing for OutputStream fails.
     *                      if Flushing contents in OutputStream fails.
     *                      if Closing OutputStream fails.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();

        final HttpRequest httpRequest;
        final Long id;
        final String requestBody;
        final HttpRequestService requestService;

        try {
            httpRequest = new HttpRequest(exchange);
            requestService = HttpRequestServiceResolver.resolve(httpRequest.getHttpMethod());
            id = httpRequest.getPath().getId();
            requestBody = httpRequest.getRequestBody();
        } catch (IllegalHttpRequestException e) {
            String message = e.getMessage();
            exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.code, message.getBytes().length);
            writeAndFlushAndClose(outputStream, message);
            return;
        }

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
