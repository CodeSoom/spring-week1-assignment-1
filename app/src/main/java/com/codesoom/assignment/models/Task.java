//TODO
//Java Bean => 완료

package com.codesoom.assignment.models;

/**
 * To do 할일을 기록하는 클래스
 */
public class Task {

    private Long id; //고유번호

    private String title; //제목

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
        return String.format("Task - title: %s", title);
    }
}
