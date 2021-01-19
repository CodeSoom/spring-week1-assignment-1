package com.codesoom.assignment.models;

/*
 ** "할 일" 저장하기 위한 클래스
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

    public String toString() {
        return "Task - title : " + title;
    }
}
