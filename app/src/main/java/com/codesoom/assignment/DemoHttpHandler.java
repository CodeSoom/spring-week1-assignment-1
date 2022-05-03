package com.codesoom.assignment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
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
	private final int id = 1;

	public DemoHttpHandler() {
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String method = exchange.getRequestMethod();
		URI uri = exchange.getRequestURI();
		String path = uri.getPath();
		String idStr = path.substring(path.lastIndexOf('/') + 1);
		InputStream inputStream = exchange.getRequestBody();
		String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
			.collect(Collectors.joining("\n"));
		String content = "Hello world" + " " + path;

		if (method.equals("GET") && path.equals("/tasks") && !canConvertId(idStr)) {
			content = tasksToJSON();
			exchange.sendResponseHeaders(200, content.getBytes().length);
		} else if (method.equals("GET") && canConvertId(idStr)) {
			content = taskToJSON(new Long(idStr));
			exchange.sendResponseHeaders(200, content.getBytes().length);
		} else if (method.equals("POST") && path.equals("/tasks")) {
			if (!body.isEmpty()) {
				Task task = toTask(body);
				tasks.add(task);
			}
			content = tasksToJSON();
			exchange.sendResponseHeaders(201, content.getBytes().length);
		} else if (method.equals("PUT") && canConvertId(idStr)) {
			if (!body.isEmpty()) {
				Long id = new Long(idStr);
				Task task = tasks.stream()
					.filter(t -> t.getId().equals(id))
					.findAny()
					.orElseThrow(IllegalArgumentException::new);
				Task changeTask = toTask(body);
				task.setTitle(changeTask.getTitle());
			}
			exchange.sendResponseHeaders(200, content.getBytes().length);
			content = tasksToJSON();
		} else if (method.equals("DELETE") && canConvertId(idStr)) {
			Long id = new Long(idStr);
			Task task = tasks.stream()
				.filter(t -> t.getId().equals(id))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
			tasks.remove(task);
			content = tasksToJSON();
			exchange.sendResponseHeaders(204, -1);
		}

		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	private boolean canConvertId(String idStr) {
		try {
			Integer.parseInt(idStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Task toTask(String content) throws JsonProcessingException {
		Task task = objectMapper.readValue(content, Task.class);
		task.setId(new Long(id));
		id += 1;
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
}
