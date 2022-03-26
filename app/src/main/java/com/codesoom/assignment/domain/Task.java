package com.codesoom.assignment.domain;

import com.google.common.base.MoreObjects;

/**
 * 할 일을 표현하는 클래스
 * ID 와 TITLE 을 통해 표현한다.
 */
public class Task {
    public static long taskSequence = 0;
    private final Long id;
    private String title;

    private Task() {
        this.id = ++taskSequence;
    }

    public Task(String title) {
        this.id = ++taskSequence;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .toString();
    }
}
