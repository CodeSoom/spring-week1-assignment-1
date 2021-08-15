package com.codesoom.assignment.modles;

import com.fasterxml.jackson.annotation.JacksonInject;

public final class Task {

    private final Long id;
    private String title;

    private Task(@JacksonInject final Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }
}
