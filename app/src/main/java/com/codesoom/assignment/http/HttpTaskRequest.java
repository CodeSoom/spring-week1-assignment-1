package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

public class HttpTaskRequest extends HttpRequest {

	public HttpTaskRequest(HttpExchange httpExchange) {
		super(httpExchange);
	}

	public int taskId() {
		String[] pathSegments = getPath().split("/");

		return Integer.parseInt(pathSegments[pathSegments.length - 1]);
	}
}
