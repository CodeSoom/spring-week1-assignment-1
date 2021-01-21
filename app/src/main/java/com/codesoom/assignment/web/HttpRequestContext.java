package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpRequestMethod;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class HttpRequestContext {

    private final StrategyProcess nullStrategy = (httpRequest, httpExchange, taskService) ->
            HttpResponseTransfer.sendResponse(HttpStatusCode.NOT_FOUND, httpExchange);
    private final Map<HttpRequestMethod, StrategyProcess> requestStrategyProcessMap;

    public HttpRequestContext(Map<HttpRequestMethod, StrategyProcess> requestStrategyProcessMap) {
        this.requestStrategyProcessMap = requestStrategyProcessMap;
    }

    public void processRequest(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException {
        if (httpRequest.isServerHealthCheck()) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.OK, httpExchange);
            return;
        }

        if (httpRequest.isInvalidPath()) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.NOT_FOUND, httpExchange);
            return;
        }

        StrategyProcess strategy = findStrategy(httpRequest.getMethod());
        try {
            strategy.process(httpRequest, httpExchange, taskService);
        } catch (NumberFormatException e) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.INVALID_PARAMETER, httpExchange);
        } catch (IllegalArgumentException e) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.NOT_FOUND, httpExchange);
        } catch (Exception e) {
            HttpResponseTransfer.sendResponse(HttpStatusCode.INTERNAL_ERROR, httpExchange);
        }
    }

    private StrategyProcess findStrategy(HttpRequestMethod method) {
        return requestStrategyProcessMap.getOrDefault(method, nullStrategy);
    }

}
