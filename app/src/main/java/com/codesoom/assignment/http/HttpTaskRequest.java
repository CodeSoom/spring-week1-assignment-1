package com.codesoom.assignment.http;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.util.JsonObjectMapper;
import com.codesoom.assignment.util.URIPatternMatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

public class HttpTaskRequest extends HttpRequest {

	public HttpTaskRequest(HttpExchange httpExchange) {
		super(httpExchange);
	}

	public int taskId() {
		String taskId = URIPatternMatcher.getBasicResourceId(getPath());

		return Integer.parseInt(taskId);
	}

	public Task bodyToTask() throws JsonProcessingException {
		String requestBody = getBody();

		return JsonObjectMapper.toObject(requestBody, Task.class);
	}
}
