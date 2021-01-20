package com.codesoom.assignment.resources;

import java.io.IOException;

public interface ResourceHandler {

    String handleRequest(String path, String body) throws IOException;
}
