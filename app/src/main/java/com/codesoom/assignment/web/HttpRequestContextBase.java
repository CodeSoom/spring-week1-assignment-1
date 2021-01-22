package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequestContextBase {
    private final RequestControllable nullController = httpRequest -> new HttpResponse(HttpStatusCode.NOT_FOUND);
    protected final Map<HttpRequestMethod, RequestControllable> requestControllerMap = new HashMap<>();

    public void processHttpRequest(HttpRequest httpRequest, HttpExchange httpExchange) throws IOException {
        RequestControllable controller = findController(httpRequest.getMethod());
        HttpResponse httpResponse;
        try {
            httpResponse = controller.process(httpRequest);
        } catch (NumberFormatException e) {
            httpResponse = new HttpResponse(HttpStatusCode.INVALID_PARAMETER);
        } catch (IllegalArgumentException e) {
            httpResponse = new HttpResponse(HttpStatusCode.NOT_FOUND);
        } catch (Exception e) {
            httpResponse = new HttpResponse(HttpStatusCode.INTERNAL_ERROR);
        }
        HttpResponseTransfer.sendResponse(httpResponse, httpExchange);
    }

    protected RequestControllable findController(HttpRequestMethod method) {
        return requestControllerMap.getOrDefault(method, nullController);
    }
}
