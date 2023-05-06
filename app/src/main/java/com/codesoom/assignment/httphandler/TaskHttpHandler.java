package com.codesoom.assignment.httphandler;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.http.*;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.IdGenerator;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskHttpHandler implements HttpHandler {
	private static final List<Task> tasks = new ArrayList<>();

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		HttpTaskRequest httpRequest = new HttpTaskRequest(httpExchange);

		String requestMethod = httpRequest.getMethod();
		String path = httpRequest.getPath();

		if ("GET".equals(requestMethod) && "/tasks".equals(path)) {
			getAllTasks(httpRequest);
		} else if ("GET".equals(requestMethod) && path.startsWith("/tasks/")) {
			getTask(httpRequest);
		} else if ("POST".equals(requestMethod)) {
			createTask(httpRequest);
		} else if ("PUT".equals(requestMethod)) {
			putTask(httpRequest);
		} else if ("DELETE".equals(requestMethod)) {
			deleteTask(httpRequest);
		}
	}

	private void deleteTask(HttpTaskRequest httpRequest) throws IOException {
		int taskId = httpRequest.taskId();
		try {
			Task task = findTask(taskId);
			tasks.remove(task);

			new HttpNoContentResponse(httpRequest.getHttpExchange()).send();
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	private void putTask(HttpTaskRequest httpRequest) throws IOException {
		int taskId = httpRequest.taskId();

		try {
			Task task = findTask(taskId);
			String requestBody = httpRequest.getBody();
			Task request = JsonObjectMapper.toObject(requestBody, Task.class);
			task.update(request);

			new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	private void getTask(HttpTaskRequest httpRequest) throws IOException {
		int taskId = httpRequest.taskId();
		try {
			Task task = findTask(taskId);

			new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	public void getAllTasks(HttpTaskRequest httpRequest) throws IOException {
		new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJsonArray(tasks));
	}

	public void createTask(HttpTaskRequest httpRequest) throws IOException {
		String requestBody = httpRequest.getBody();

		Task task = JsonObjectMapper.toObject(requestBody, Task.class);
		task.setId(IdGenerator.genId(IdGenerator.IdType.TASK));
		tasks.add(task);

		new HttpCreatedResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
	}
	private Task findTask(int taskId) throws TaskNotFoundException {
		return tasks.stream()
				.filter(task -> task.getId() == taskId)
				.findFirst().orElseThrow(() -> new TaskNotFoundException(taskId));
	}

}
