package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.ParameterNotFoundException;

public class Path {
    private final String resource;
    private final String pathVariable;

    public Path(String path){
        String[] pathArr = path.split("/");
        this.resource = pathArr[1];
        this.pathVariable = pathArr.length >= 3 ? pathArr[2] : null;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Path{");
        sb.append("resource='").append(resource).append('\'');
        sb.append(", pathVariable='").append(pathVariable).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
