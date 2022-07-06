package com.codesoom.assignment.network;

import java.io.IOException;

public interface RouterExecutable {
    public void execute(HttpRequest request, HttpResponse response) throws IOException;
}
