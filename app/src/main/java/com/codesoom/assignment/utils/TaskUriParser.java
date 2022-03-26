package com.codesoom.assignment.utils;

import java.util.Arrays;
import java.util.List;

public class TaskUriParser {

    private static final String RESOURCE_NAME = "tasks";

    private static final int RESOURCE_POSITION = 1;

    private static final int RESOURCE_ID_POSITION = 2;

    private static final int HAS_ID_PATHS_LENGTH = 3;

    private static final List<Integer> ALLOW_PATH_LENGTH = Arrays.asList(2, 3);

    private final String[] pathSegments;

    public TaskUriParser(final String pathUri) {
        this.pathSegments = pathUri.split("/");
    }

    public boolean isInvalidPath() {

        if (!ALLOW_PATH_LENGTH.contains(pathSegments.length)) {
            return true;
        }

        if (pathSegments[RESOURCE_POSITION].equals(RESOURCE_NAME)) {
            return false;
        }
        return true;
    }

    public boolean hasId() {
        return pathSegments.length == HAS_ID_PATHS_LENGTH;
    }

    public boolean hasNotId() {
        return !hasId();
    }

    public Long getId() {
        return Long.valueOf(pathSegments[RESOURCE_ID_POSITION]);
    }
}
