package com.codesoom.assignment.http;

import com.codesoom.assignment.http.HttpResponse;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpNotFoundResponse extends HttpResponse {

	private final static int STATUS_CODE = 404;

	public HttpNotFoundResponse(HttpExchange exchange) {
		super(exchange, STATUS_CODE);
	}

}
