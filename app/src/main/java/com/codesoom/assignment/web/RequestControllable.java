package com.codesoom.assignment.web;

import java.io.IOException;

public interface RequestControllable {
    HttpResponse process(HttpRequest httpRequest) throws IOException;
}
