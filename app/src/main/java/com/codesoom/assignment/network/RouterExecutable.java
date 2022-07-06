package com.codesoom.assignment.network;

import java.io.IOException;

public interface RouterExecutable {
    void execute(HttpRequest request, HttpResponse response) throws IOException;
}
