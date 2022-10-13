package com.codesoom.assignment;

import java.net.URI;

public class Path {

    private final URI uri;
    private final String path;
    private final Long id;

    public Path(URI uri) {
        this.uri = uri;
        this.path = uri.getPath();
        id = resolveId(this.path);
    }

    private Long resolveId(String path) {
        String[] pathArr = path.split("/");
        if (pathArr.length != 3) {
            return null;
        }

        return Long.valueOf(pathArr[2]);
    }

    public boolean isValid() {
        return path != null;
    }

    public String getPath() {
        return path;
    }

    public Long getId() {
        return id;
    }
}
