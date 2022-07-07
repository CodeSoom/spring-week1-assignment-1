package com.codesoom.assignment.network;

import java.io.IOException;

public interface HttpRouterExecutable {
    void execute(HttpRequest request, HttpResponse response) throws IOException;
}
