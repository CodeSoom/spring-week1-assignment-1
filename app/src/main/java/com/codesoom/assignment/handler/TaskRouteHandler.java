package com.codesoom.assignment.handler;

import java.io.IOException;

public interface TaskRouteHandler {

    boolean isSelect(String method, String path);

    void execute(HttpRequest request, HttpResponse response) throws IOException;

}
