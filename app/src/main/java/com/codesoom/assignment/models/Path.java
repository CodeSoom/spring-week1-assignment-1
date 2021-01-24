package com.codesoom.assignment.models;

import java.util.OptionalLong;

public class Path {
    String path;
    String target = "/tasks/";
    public Path(String path) {
        this.path = path;
    }

    public Long extractNumber() {
        path = path.replace(target, "");
        return OptionalLong.of(Long.parseLong(path)).getAsLong();
    }
    public boolean checkUrl(){
        return path.equals("/") || path.startsWith("/tasks");
    }

    public boolean hasNumberParameter() {
        path = path.replace(target, "");
        return path.matches("^[0-9]+$");
    }
}
