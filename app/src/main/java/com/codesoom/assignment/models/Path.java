package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.ParameterNotFoundException;

public class Path {
    public final String fullPath;
    public final String resource;
    public final String pathVariable;

    public Path(String path){
        this.fullPath = path;
        String[] pathArr = path.split("/");
        this.resource = pathArr.length >= 2 ? pathArr[1] : null;
        this.pathVariable = pathArr.length >= 3 ? pathArr[2] : null;
    }

    public boolean hasPathVariable(){
        return this.pathVariable != null;
    }
    public boolean hasResource(){
        return this.resource != null;
    }

    public boolean resourceEquals(String resource){
        return this.resource.equals(resource);
    }

}
