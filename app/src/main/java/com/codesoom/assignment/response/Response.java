package com.codesoom.assignment.response;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

public abstract class Response {

	private final HttpExchange exchange;

	public Response(HttpExchange exchange) {
		this.exchange = exchange;
	}

	public void send(String content) throws IOException {
		exchange.sendResponseHeaders(getHttpStatusCode(), content.getBytes().length);
		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	protected abstract int getHttpStatusCode();
}
