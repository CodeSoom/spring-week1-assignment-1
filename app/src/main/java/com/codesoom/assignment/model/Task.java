package com.codesoom.assignment.model;

public class Task {

	private int id;
	private String title;

	public Task() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Task update(Task target) {
		this.title = target.getTitle();
		return this;
	}

	@Override
	public String toString() {
		return String.format("Task { id = %s, title = %s}", id, title);
	}
}
