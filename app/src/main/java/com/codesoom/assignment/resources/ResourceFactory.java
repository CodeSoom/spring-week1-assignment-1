package com.codesoom.assignment.resources;

import com.codesoom.assignment.HttpMethod;

public class ResourceFactory {

    public TaskResource createResource(HttpMethod method) {
        TaskResource taskResource = null;
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
