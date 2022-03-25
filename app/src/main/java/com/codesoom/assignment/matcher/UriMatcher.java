package com.codesoom.assignment.matcher;

public interface UriMatcher {

    boolean hasId(String[] paths);

    boolean isValidPath(String[] paths);

    Long getId(String[] paths);
}
