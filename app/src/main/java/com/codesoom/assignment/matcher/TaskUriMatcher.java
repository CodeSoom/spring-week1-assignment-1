package com.codesoom.assignment.matcher;

import java.util.Arrays;
import java.util.List;

public class TaskUriMatcher implements UriMatcher {

    private static final String RESOURCE_NAME = "tasks";

    private static final int RESOURCE_POSITION = 1;

    private static final int RESOURCE_ID_POSITION = 2;

    private static final int HAS_ID_PATHS_LENGTH = 3;

    private static final List<Integer> ALLOW_PATH_LENGTH = Arrays.asList(2, 3);

    @Override
    public boolean hasId(final String[] pathSegments) {
        return pathSegments.length == HAS_ID_PATHS_LENGTH;
    }

    @Override
    public boolean isInvalidPath(final String[] pathSegments) {

        if (!ALLOW_PATH_LENGTH.contains(pathSegments.length)) {
            return true;
        }

        if (pathSegments[RESOURCE_POSITION].equals(RESOURCE_NAME)) {
            return false;
        }
        return true;
    }

    @Override
    public Long getId(final String[] pathSegments) {
        return Long.valueOf(pathSegments[RESOURCE_ID_POSITION]);
    }
}
