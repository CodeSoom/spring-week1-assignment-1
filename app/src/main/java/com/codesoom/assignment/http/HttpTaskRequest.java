package com.codesoom.assignment.http;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

public class HttpTaskRequest extends HttpRequest {

	public HttpTaskRequest(HttpExchange httpExchange) {
		super(httpExchange);
	}

	public int taskId() {
		String[] pathSegments = getPath().split("/");

		return Integer.parseInt(pathSegments[pathSegments.length - 1]);
	}

	public Task bodyToTask() throws JsonProcessingException {
		String requestBody = getBody();

		return JsonObjectMapper.toObject(requestBody, Task.class);
	}
}
