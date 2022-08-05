package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.ParameterNotFoundException;

public class Path {
    private final String fullPath;
    private final String resource;
    private final String pathVariable;

    public Path(String path){
        this.fullPath = path;
        String[] pathArr = path.split("/");
        this.resource = pathArr.length >= 2 ? pathArr[1] : null;
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

    public boolean hasResource(){
        return this.resource != null;
    }

    public boolean resourceEquals(String resource){
        return this.resource.equals(resource);
    }

}
