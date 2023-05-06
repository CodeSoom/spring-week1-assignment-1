package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpNoContentResponse extends HttpResponse {

	private final static int STATUS_CODE = 204;

	public HttpNoContentResponse(HttpExchange exchange) {
		super(exchange, STATUS_CODE);
	}

}
