package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;

public class ResourceFactory {

    public TaskResource createResource(HttpMethod method) {
        TaskResource taskResource = null;
        List<Task> tasks = new ArrayList<>();
        switch (method) {
            case GET:
                taskResource = new GetTaskResource();
                break;
            case POST:
                taskResource = new PostTaskResource();
            //TODO: PUT, DELETE
        }
        return taskResource;
    }
}
