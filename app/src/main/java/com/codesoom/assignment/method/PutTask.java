package com.codesoom.assignment.method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseOK;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

public class PutTask extends DoTask {
	public PutTask(HttpExchange exchange) {
		super(exchange);
	}

	@Override
	public void handleItem() throws IOException {
		InputStream inputStream = exchange.getRequestBody();
		String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
			.collect(Collectors.joining("\n"));
		if (!body.isEmpty()) {
			Long id = getIdFromPath(exchange);
			Task task = tasks.stream()
				.filter(t -> t.getId().equals(id))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
			Task changeTask = contentToTask(body);
			task.setTitle(changeTask.getTitle());
		}
		String content = tasksToJSON();
		new ResponseOK(exchange).send(content);
	}

	protected Task contentToTask(String content) throws JsonProcessingException {
		Task task = objectMapper.readValue(content, Task.class);
		task.setId(id);
		return task;
	}
}
