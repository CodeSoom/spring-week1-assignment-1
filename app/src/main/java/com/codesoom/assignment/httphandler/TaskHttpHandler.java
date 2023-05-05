package com.codesoom.assignment.httphandler;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
	private static final List<Task> tasks = new ArrayList<>();

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		String path = httpExchange.getRequestURI().getPath();

		if ("GET".equals(requestMethod) && "/tasks".equals(path)) {
			getAllTasks(httpExchange);
		} else if ("GET".equals(requestMethod) && path.startsWith("/tasks/")) {
			getTask(httpExchange);
		} else if ("POST".equals(requestMethod)) {
			createTask(httpExchange);
		} else if ("PUT".equals(requestMethod)) {
			putTask(httpExchange);
		} else if ("DELETE".equals(requestMethod)) {
			deleteTask(httpExchange);
		}
	}

	private void deleteTask(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		int taskId = extractTaskIdFromPath(path);
		try {
			Task task = findTask(taskId);
			tasks.remove(task);

			sendResponse(httpExchange, 200, null);
		} catch (TaskNotFoundException e) {
			sendResponse(httpExchange, 400, e.getMessage());
		}
	}

	private void putTask(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		int taskId = extractTaskIdFromPath(path);

		try {
			Task task = findTask(taskId);
			String requestBody = parseRequestBody(httpExchange);
			Task request = JsonObjectMapper.toObject(requestBody, Task.class);
			task.update(request);

			sendResponse(httpExchange, 200, JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			sendResponse(httpExchange, 400, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void getTask(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		int taskId = extractTaskIdFromPath(path);
		try {
			Task task = findTask(taskId);

			sendResponse(httpExchange, 200, JsonObjectMapper.toJson(task));
		} catch (TaskNotFoundException e) {
			sendResponse(httpExchange, 400, e.getMessage());
		}
	}

	public void getAllTasks(HttpExchange httpExchange) throws IOException {
		sendResponse(httpExchange, 200, JsonObjectMapper.toJsonArray(tasks));
	}

	public void createTask(HttpExchange httpExchange) throws IOException {
		String requestBody = parseRequestBody(httpExchange);

		Task task = JsonObjectMapper.toObject(requestBody, Task.class);
		task.setId(IdGenerator.genId(IdGenerator.IdType.TASK));
		tasks.add(task);

		sendResponse(httpExchange, 201, JsonObjectMapper.toJson(task));
	}

	private int extractTaskIdFromPath(String path) {
		String[] pathSegments = path.split("/");

		return Integer.parseInt(pathSegments[pathSegments.length - 1]);
	}

	private Task findTask(int taskId) throws TaskNotFoundException {
		return tasks.stream()
				.filter(task -> task.getId() == taskId)
				.findFirst().orElseThrow(() -> new TaskNotFoundException(taskId));
	}

	public String parseRequestBody(HttpExchange httpExchange) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
		return bufferedReader.lines().collect(Collectors.joining("\n"));
	}

	private void sendResponse(HttpExchange httpExchange, int httpCode, String response) throws IOException {
		if (response != null) {
			httpExchange.sendResponseHeaders(httpCode, response.getBytes().length);
			httpExchange.getResponseBody().write(response.getBytes());
			httpExchange.getResponseBody().close();
		} else {
			httpExchange.sendResponseHeaders(httpCode, 0);
			httpExchange.close();
		}
	}
}
