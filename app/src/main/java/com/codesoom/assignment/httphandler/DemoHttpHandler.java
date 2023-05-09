package com.codesoom.assignment.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String response = "It's OK";
		httpExchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream responseBody = httpExchange.getResponseBody();
		responseBody.write(response.getBytes());
		responseBody.close();
	}




}
