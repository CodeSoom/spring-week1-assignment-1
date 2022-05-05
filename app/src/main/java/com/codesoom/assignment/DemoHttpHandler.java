package com.codesoom.assignment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DemoHttpHandler implements HttpHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final List<Task> tasks = new ArrayList<>();
	private int id = 1;
	private static int OK = 200;
	private static int NO_CONTENT = 204;
	private static int CREATED = 201;

	protected DemoHttpHandler() {
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String method = exchange.getRequestMethod();
		if (method.equals("GET")) {
			getTask(exchange);
		} else if (method.equals("POST")) {
			createTask(exchange);
		} else if (method.equals("PUT")) {
			updateTask(exchange);
		} else if (method.equals("DELETE")) {
			deleteTask(exchange);
		}
	}

	private void deleteTask(HttpExchange exchange) throws IOException {
		if (isStartWithTasks(exchange)) {
			Long id = getIdFromPath(exchange);
			Task task = tasks.stream()
				.filter(t -> t.getId().equals(id))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
			tasks.remove(task);
			String content = tasksToJSON();
			exchange.sendResponseHeaders(NO_CONTENT, -1);
			createOutputStream(exchange, content);
		}
	}

	private void updateTask(HttpExchange exchange) throws IOException {
		if (isStartWithTasks(exchange)) {
			InputStream inputStream = exchange.getRequestBody();
			String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
				.collect(Collectors.joining("\n"));
			if (!body.isEmpty()) {
				Long id = getIdFromPath(exchange);
				Task task = tasks.stream()
					.filter(t -> t.getId().equals(id))
					.findAny()
					.orElseThrow(IllegalArgumentException::new);
				Task changeTask = contentToTask(body, false);
				task.setTitle(changeTask.getTitle());
			}
			String content = tasksToJSON();
			exchange.sendResponseHeaders(OK, content.getBytes().length);
			createOutputStream(exchange, content);
		}
	}

	private void createTask(HttpExchange exchange) throws IOException {
		if (isStartWithTasks(exchange)) {
			InputStream inputStream = exchange.getRequestBody();
			String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
				.collect(Collectors.joining("\n"));
			if (!body.isEmpty()) {
				Task task = contentToTask(body, true);
				tasks.add(task);
			}
			String content = tasksToJSON();
			exchange.sendResponseHeaders(CREATED, content.getBytes().length);
			createOutputStream(exchange, content);
		}
	}

	private void getTask(HttpExchange exchange) throws IOException {
		if (isStartWithTasks(exchange)) {
			String path = exchange.getRequestURI().getPath();
			if (path.equals("/tasks") || path.equals("/tasks/")) {
				getAllTasks(exchange);
			} else {
				getOneTask(exchange);
			}
		}
	}

	private void getAllTasks(HttpExchange exchange) throws IOException {
		String content = tasksToJSON();
		exchange.sendResponseHeaders(OK, content.getBytes().length);
		createOutputStream(exchange, content);
	}

	private void createOutputStream(HttpExchange exchange, String content) throws IOException {
		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	private void getOneTask(HttpExchange exchange) throws IOException {
		String content = taskToJSON(getIdFromPath(exchange));
		exchange.sendResponseHeaders(OK, content.getBytes().length);
		createOutputStream(exchange, content);
	}

	private Long getIdFromPath(HttpExchange exchange) {
		String idString = exchange.getRequestURI().getPath().substring("/tasks/".length());
		canConvertId(idString);
		return new Long(idString);
	}

	private void canConvertId(String idString) {
		try {
			new Long(idString);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	private Task contentToTask(String content, boolean isPostMethod) throws JsonProcessingException {
		Task task = objectMapper.readValue(content, Task.class);
		task.setId(new Long(id));
		if (isPostMethod) {
			id += 1;
		}
		return task;
	}

	private String taskToJSON(Long id) throws IOException {
		OutputStream outputStream = new ByteArrayOutputStream();
		Task task = tasks.stream().filter(t -> t.getId().equals(id)).findAny().orElseThrow(IllegalAccessError::new);
		objectMapper.writeValue(outputStream, task);
		return outputStream.toString();
	}

	private String tasksToJSON() throws IOException {
		OutputStream outputStream = new ByteArrayOutputStream();
		objectMapper.writeValue(outputStream, tasks);
		return outputStream.toString();
	}

	private boolean isStartWithTasks(HttpExchange exchange) {
		return exchange.getRequestURI().getPath().startsWith("/tasks/");
	}
}
