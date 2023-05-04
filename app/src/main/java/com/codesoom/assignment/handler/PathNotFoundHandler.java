package com.codesoom.assignment.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.NOT_FOUND;
import static com.codesoom.assignment.util.HttpExchangeUtil.*;

public class PathNotFoundHandler implements TaskRouteHandler {

    @Override
    public boolean isSelect(final String method, final String path) {
        return false;
    }

    @Override
    public void execute(final HttpExchange exchange) throws IOException {
        String errorMessage = String.format(
                "404 Not Found: The requested path '%s' with method '%s' does not exist.",
                getRequestPath(exchange),
                getRequestMethod(exchange)
        );
        sendHttpResponse(exchange, NOT_FOUND.getCode(), errorMessage);
    }

}
