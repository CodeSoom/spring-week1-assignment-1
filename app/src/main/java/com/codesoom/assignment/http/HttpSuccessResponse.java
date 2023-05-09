package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpSuccessResponse extends HttpResponse {

	private final static int STATUS_CODE = 200;

	public HttpSuccessResponse(HttpExchange exchange) {
		super(exchange, STATUS_CODE);
	}

}
