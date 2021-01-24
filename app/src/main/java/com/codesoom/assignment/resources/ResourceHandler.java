package com.codesoom.assignment.resources;

import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.List;

public interface ResourceHandler {

    Response handleRequest(List<Task> tasks, String path, String body) throws IOException;
}
