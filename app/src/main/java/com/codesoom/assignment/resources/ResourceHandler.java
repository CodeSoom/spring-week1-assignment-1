package com.codesoom.assignment.resources;

import com.codesoom.assignment.models.Response;

import java.io.IOException;

public interface ResourceHandler {

    Response handleRequest(String path, String body) throws IOException;
}
