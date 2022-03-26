package com.codesoom.assignment.matcher;

public interface UriMatcher {

    boolean hasId(String[] paths);

    boolean isInvalidPath(String[] paths);

    Long getId(String[] paths);
}
