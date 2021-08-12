package com.codesoom.assignment.models;

public class RequestContent {
    private String title;

    private static final String EMPTY_TITLE = "";

    public RequestContent() {
        this.title = EMPTY_TITLE;
    }

    public RequestContent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
