package com.codesoom.assignment;

/**
 * Model class about task having id and title
 *
 * @author Taeheon Woo
 * @version 1.0
 *
 */
public class Task {
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
