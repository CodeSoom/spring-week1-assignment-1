package com.codesoom.assignment.enums;

public enum ContextContainer {

    TASKS("/tasks"),
    NOT_EXIST(""),
    ;

    private String context;

    public String getContextValue() {
        return context;
    }

    ContextContainer(String context) {
        this.context = context;
    }

    public static ContextContainer getContext(String uriPath) {
        for (ContextContainer context : ContextContainer.values()) {
            if (uriPath.startsWith(context.getContextValue())) {
                return context;
            }
        }

        return NOT_EXIST;
    }
}
