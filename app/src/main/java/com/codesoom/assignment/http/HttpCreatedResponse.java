package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

public class HttpCreatedResponse extends HttpResponse {

	private final static int STATUS_CODE = 201;

	public HttpCreatedResponse(HttpExchange exchange) {
		super(exchange, STATUS_CODE);
	}

}
