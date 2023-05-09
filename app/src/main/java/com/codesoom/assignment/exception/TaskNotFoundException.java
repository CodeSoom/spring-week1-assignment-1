package com.codesoom.assignment.exception;

public class TaskNotFoundException extends Exception {
	public TaskNotFoundException(int taskId) {
		super(String.format("Task not found : %d", taskId));
	}
}
