package com.codesoom.assignment.http;

import com.codesoom.assignment.util.URIPatternMatcher;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

	private final HttpExchange httpExchange;

	public HttpRequest(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	public String getPath() {
		return httpExchange.getRequestURI().getPath();
	}

	public String getMethod() {
		return httpExchange.getRequestMethod();
	}

	public HttpExchange getHttpExchange() {
		return httpExchange;
	}

	public String getBody() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
		return bufferedReader.lines().collect(Collectors.joining("\n"));
	}

	public boolean isRequestRoot() {
		return URIPatternMatcher.requestRoot(getPath());
	}
}
