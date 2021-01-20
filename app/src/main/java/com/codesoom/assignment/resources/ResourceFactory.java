package com.codesoom.assignment.resources;

public class ResourceFactory {

    public TaskResource createResource(String method) {
        TaskResource taskResource = null;
        switch (method) {
            case "GET":
                taskResource = new GetTaskResource();
                break;
            case "POST":
                taskResource = new PostTaskResource();
            //TODO: PUT, DELETE
        }
        return taskResource;
    }
}
