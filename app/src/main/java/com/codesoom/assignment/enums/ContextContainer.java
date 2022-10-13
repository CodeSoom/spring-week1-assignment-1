package com.codesoom.assignment.enums;

import com.codesoom.assignment.models.Path;

import java.util.Arrays;

public enum ContextContainer {

    TASKS("tasks"),
    NOT_EXIST(""),
    ;

    private String context;

    public String getContextValue() {
        return context;
    }

    ContextContainer(String context) {
        this.context = context;
    }

    public static ContextContainer getContext(Path path) {
        return Arrays.stream(ContextContainer.values())
                .filter(context -> context.getContextValue().equals(path.getPath()))
                .findFirst()
                .orElse(ContextContainer.NOT_EXIST);
    }
}
