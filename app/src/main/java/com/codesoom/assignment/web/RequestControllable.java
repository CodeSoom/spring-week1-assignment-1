package com.codesoom.assignment.web;

import java.io.IOException;
import java.util.Optional;

public interface RequestControllable {
    Optional<HttpResponse> process(HttpRequest httpRequest) throws IOException;
}
