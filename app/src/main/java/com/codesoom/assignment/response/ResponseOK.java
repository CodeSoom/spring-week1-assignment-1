package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseOK extends Response {
	private static final int OK = 200;

	public ResponseOK(HttpExchange exchange) {
		super(exchange);
	}

	@Override
	protected int getHttpStatusCode() {
		return OK;
	}
}
