package com.codesoom.assignment.web;

import com.codesoom.assignment.application.JsonMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequestContextBase {
    private final RequestControllable nullController = httpRequest -> new HttpResponse(HttpStatusCode.NOT_FOUND);
    protected final Map<HttpRequestMethod, RequestControllable> requestControllerMap = new HashMap<>();
    protected final JsonMapper jsonMapper = new JsonMapper();
    protected final String basePath;

    public abstract boolean matchesPath(String path);

    public HttpRequestContextBase(String basePath) {
        this.basePath = basePath;
    }

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

    protected long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지ll막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace(basePath, "").replaceAll("/", "");
        return Long.parseLong(idString);
    }
}
