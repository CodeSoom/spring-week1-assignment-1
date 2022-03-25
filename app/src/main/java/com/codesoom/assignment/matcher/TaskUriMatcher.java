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
    public boolean hasId(final String[] paths) {
        return paths.length == HAS_ID_PATHS_LENGTH;
    }

    @Override
    public boolean isValidPath(final String[] paths) {

        if (!ALLOW_PATH_LENGTH.contains(paths.length)) {
            return false;
        }
        return paths[RESOURCE_POSITION].equals(RESOURCE_NAME);
    }

    @Override
    public Long getId(final String[] paths) {
        return Long.valueOf(paths[RESOURCE_ID_POSITION]);
    }
}
