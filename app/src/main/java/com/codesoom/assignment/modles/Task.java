package com.codesoom.assignment.modles;

public final class Task {
    private static Long CURRENT_ID = 1L;

    private final Long id;
    private String title;

    public Task() {
        this.id = CURRENT_ID;
        ++CURRENT_ID;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
