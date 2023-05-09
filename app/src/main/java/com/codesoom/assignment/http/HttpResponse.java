package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public abstract class HttpResponse implements HttpResponseSend{

	private HttpExchange httpExchange;
	private int statusCode;

	protected HttpResponse(HttpExchange httpExchange, int statusCode) {
		this.httpExchange = httpExchange;
		this.statusCode = statusCode;
	}

	public void send() throws IOException {
		httpExchange.sendResponseHeaders(statusCode, 0);
		httpExchange.close();
	}

	public void send(String responseBody) throws IOException {
		httpExchange.sendResponseHeaders(statusCode, responseBody.getBytes().length);
		httpExchange.getResponseBody().write(responseBody.getBytes());
		httpExchange.getResponseBody().close();
	}

}
