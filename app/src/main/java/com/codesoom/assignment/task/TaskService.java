package com.codesoom.assignment.task;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

	private static List<Task> tasks;

	public TaskService() {
		tasks = new ArrayList<>();
	}

	public List<Task> getAllTasks() {
		return tasks;
	}

	public Task findTask(int taskId) throws TaskNotFoundException {
		return tasks.stream()
				.filter(task -> task.getId() == taskId)
				.findFirst().orElseThrow(() -> new TaskNotFoundException(taskId));
	}

	public void deleteTask(int taskId) throws TaskNotFoundException {
		Task task = findTask(taskId);
		tasks.remove(task);
	}

	public Task putTask(int taskId, Task newTask) throws TaskNotFoundException {
		Task task = findTask(taskId);
		task.update(newTask);
		return task;
	}

	public Task create(Task task) throws TaskNotFoundException {
		final int taskId = IdGenerator.genId(IdGenerator.IdType.TASK);
		task.setId(taskId);
		tasks.add(task);

		return findTask(taskId);
	}
}
