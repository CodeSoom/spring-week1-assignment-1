package com.codesoom.assignment.dto;

import java.util.concurrent.atomic.AtomicLong;

public class Task {
    private static final AtomicLong count = new AtomicLong(0);
    private final Long id;
    private String title;

    public Task() {
        this.id = count.getAndIncrement();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
