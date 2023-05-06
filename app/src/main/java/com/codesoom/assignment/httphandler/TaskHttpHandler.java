package com.codesoom.assignment.httphandler;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.http.*;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.task.TaskService;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class TaskHttpHandler implements HttpHandler {
	private TaskService taskService = new TaskService();

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
			taskService.deleteTask(taskId);

			new HttpNoContentResponse(httpRequest.getHttpExchange()).send();
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	private void putTask(HttpTaskRequest httpRequest) throws IOException {
		int taskId = httpRequest.taskId();

		try {
			Task task = taskService.putTask(taskId, JsonObjectMapper.toObject(httpRequest.getBody(), Task.class));
			new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	private void getTask(HttpTaskRequest httpRequest) throws IOException {
		int taskId = httpRequest.taskId();
		try {
			Task task = taskService.findTask(taskId);

			new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

	public void getAllTasks(HttpTaskRequest httpRequest) throws IOException {
		List<Task> allTasks = taskService.getAllTasks();
		new HttpSuccessResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJsonArray(allTasks));
	}

	public void createTask(HttpTaskRequest httpRequest) throws IOException {
		try {
			Task task = taskService.create(httpRequest.bodyToTask());

			new HttpCreatedResponse(httpRequest.getHttpExchange()).send(JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			new HttpNotFoundResponse(httpRequest.getHttpExchange()).send(e.getMessage());
		}
	}

}
