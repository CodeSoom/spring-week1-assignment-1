package com.codesoom.assignment.models;

public class Path {
    String path;
    String pathVariable;

    public String getPath() {
        return path;
    }

    public String getPathVariable() {
        return pathVariable;
    }

    private void setPath(String path) {
        this.path = path;
    }

    private void setPathVariable(String pathVariable) {
        this.pathVariable = pathVariable;
    }

    public static Path createPath(String requestPath) {
        Path path = new Path();
        path.setPath(requestPath);
        return path;
    }

    public static Path setPathVariable(Path path) {
        String exceptPathVariable = getPathWithRemovedLastPath(path);
        String pathVariable = getPathVariable(path);
        path.setPath(exceptPathVariable);
        path.setPathVariable(pathVariable);
        return path;
    }

    private static String getPathVariable(Path path) {
        return path.getPath().substring(path.getPath().lastIndexOf("/") + 1);
    }

    public static String getPathWithRemovedLastPath(Path path) {
        return path.getPath().substring(0, path.getPath().lastIndexOf("/"));
    }

    public boolean isPathVariableNumeric() {
        return this.pathVariable.chars()
                .allMatch(Character::isDigit);
    }
}
