package com.codesoom.assignment.httphandler;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {
	static int taskId = 0;
	private static final List<Task> tasks = new ArrayList<>();

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestMethod = httpExchange.getRequestMethod();
		String path = httpExchange.getRequestURI().getPath();

		try {
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
		} catch (Exception e) {
			System.out.println("====== handler catch ====== ");
//			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private void deleteTask(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		int taskId = extractTaskIdFromPath(path);
		try {
			Task task = findTask(taskId);
			tasks.remove(task);
			response(httpExchange, 200, null);
		} catch (TaskNotFoundException e) {
			response(httpExchange, 400, e.getMessage());
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
			String response = JsonObjectMapper.toJson(task);
			response(httpExchange, 200, response);
		} catch (TaskNotFoundException e) {
			response(httpExchange, 400, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private void getTask(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		int taskId = extractTaskIdFromPath(path);
		try {
			Task task = findTask(taskId);
			String response = JsonObjectMapper.toJson(task);
			response(httpExchange, 200, response);
		} catch (TaskNotFoundException e) {
			response(httpExchange, 400, e.getMessage());
		}
	}

	public void getAllTasks(HttpExchange httpExchange) throws IOException {
		String response = JsonObjectMapper.toJsonArray(tasks);
		response(httpExchange, 200, response);
	}

	public void createTask(HttpExchange httpExchange) throws IOException {
		String requestBody = parseRequestBody(httpExchange);

		Task task = JsonObjectMapper.toObject(requestBody, Task.class);
		task.setId(++taskId);
		tasks.add(task);

		response(httpExchange, 201, JsonObjectMapper.toJson(task));
	}

	private int extractTaskIdFromPath(String path) {
		String[] split = path.split("/");
		return Integer.parseInt(split[split.length - 1]);
	}

	private Task findTask(int taskId) throws TaskNotFoundException {
		return tasks.stream().filter(task -> task.getId() == taskId).findFirst().orElseThrow(() -> new TaskNotFoundException(taskId));
	}

	public String parseRequestBody(HttpExchange httpExchange) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
		return bufferedReader.lines().collect(Collectors.joining("\n"));
	}

	private void response(HttpExchange httpExchange, int httpCode, String response) throws IOException {
		OutputStream responseBody = httpExchange.getResponseBody();
		if (response != null) {
			httpExchange.sendResponseHeaders(httpCode, response.getBytes().length);
			responseBody.write(response.getBytes());
		} else {
			httpExchange.sendResponseHeaders(httpCode, 0);
		}
		responseBody.close();
	}
}
