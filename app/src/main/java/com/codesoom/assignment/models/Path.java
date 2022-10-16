package com.codesoom.assignment.models;

import com.codesoom.assignment.utils.StringUtil;

public class Path {
    private final String path;
    private final String pathVariable;

    public Path(String path, String pathVariable) {
        this.path = path;

        if (pathVariable.isEmpty()) {
            this.pathVariable = null;
            return;
        }

        this.pathVariable = pathVariable.substring(1);
    }

    public String getPath() {
        return path;
    }

    public String getPathVariable() {
        return pathVariable;
    }

    public boolean isInvalidPathVariable() {
        return StringUtil.isOverThanZero(this.pathVariable);
    }
}
