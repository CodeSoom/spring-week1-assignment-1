package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.ParameterNotFoundException;

public class Path {
    private final String fullPath;
    private final String resource;
    private final String pathVariable;

    public Path(String path){
        this.fullPath = path;
        String[] pathArr = path.split("/");
        this.resource = pathArr[1];
        this.pathVariable = pathArr.length >= 3 ? pathArr[2] : null;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getResource() {
        return resource;
    }

    public String getPathVariable() throws ParameterNotFoundException {
        if(pathVariable == null){
            throw new ParameterNotFoundException("not existing pathVariable");
        }
        return pathVariable;
    }

    public boolean resourceEquals(String resource){
        return this.resource.equals(resource);
    }

}
