package com.codesoom.assignment;

import com.codesoom.assignment.routes.Response;
import com.codesoom.assignment.routes.Tasks;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

/**
 * HTTP handler.
 */
public class DemoHttpHandler implements HttpHandler {
    private static final String HEALTH_CHECK_PATH = "/";
    private static final String TASKS_PATH = "/tasks";

    private HttpExchange exchange;

    /**
     * Get request body.
     *
     * @return request body.
     */
    private String getRequestBody() {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.exchange = exchange;

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println(method + " " + uri.getPath());

        if (path.equals(HEALTH_CHECK_PATH)) {
            Response response = new Response(Response.STATUS_OK, "OK");
            response.send(exchange);
            return;
        }

        if (path.startsWith(TASKS_PATH)) {
            Tasks tasks = new Tasks();
            String body = getRequestBody();
            Response response = tasks.handler(method, path, body);
            response.send(exchange);
            return;
        }

        new Response(Response.STATUS_NOT_FOUND, "Not found").send(exchange);
    }
}
