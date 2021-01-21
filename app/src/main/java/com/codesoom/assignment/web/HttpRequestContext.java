package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpRequestMethod;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.service.StrategyProcess;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class HttpRequestContext {

    private final StrategyProcess nullStrategy = (httpRequest, taskService) -> new HttpResponse(HttpStatusCode.NOT_FOUND);
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
        HttpResponse httpResponse;
        try {
            httpResponse = strategy.process(httpRequest, taskService);
        } catch (NumberFormatException e) {
            httpResponse = new HttpResponse(HttpStatusCode.INVALID_PARAMETER);
        } catch (IllegalArgumentException e) {
            httpResponse = new HttpResponse(HttpStatusCode.NOT_FOUND);
        } catch (Exception e) {
            httpResponse = new HttpResponse(HttpStatusCode.INTERNAL_ERROR);
        }
        HttpResponseTransfer.sendResponse(httpResponse, httpExchange);
    }

    private StrategyProcess findStrategy(HttpRequestMethod method) {
        return requestStrategyProcessMap.getOrDefault(method, nullStrategy);
    }

}
