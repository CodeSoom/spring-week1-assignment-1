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
		sendResponse();
	}

	public void send(String responseBody) throws IOException {
		sendResponse(responseBody);
	}

	private void sendResponse(String responseBody) throws IOException {
		httpExchange.sendResponseHeaders(statusCode, responseBody.getBytes().length);
		httpExchange.getResponseBody().write(responseBody.getBytes());
		httpExchange.getResponseBody().close();
	}

	private void sendResponse() throws IOException {
		httpExchange.sendResponseHeaders(statusCode, 0);
		httpExchange.close();
	}

}
