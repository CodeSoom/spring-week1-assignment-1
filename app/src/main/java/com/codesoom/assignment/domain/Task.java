package com.codesoom.assignment.domain;

/**
 * 할 일을 표현하는 클래스
 * ID 와 TITLE 을 통해 표현한다.
 */
public class Task {
    private final Long id;
    private String title;

    public Task(Long id, String title) {
        this.id = id;
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
}
