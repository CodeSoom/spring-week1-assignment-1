package com.codesoom.assignment.models;

import java.util.concurrent.atomic.AtomicLong;

public class Task {

    private static final AtomicLong atomicLong = new AtomicLong(1L);
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId() {
        this.id = atomicLong.getAndIncrement();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
