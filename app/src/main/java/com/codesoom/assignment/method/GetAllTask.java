package com.codesoom.assignment.method;

import java.io.IOException;

import com.codesoom.assignment.response.ResponseOK;
import com.sun.net.httpserver.HttpExchange;

public class GetAllTask extends DoTask {
	public GetAllTask(HttpExchange exchange) {
		super(exchange);
	}

	@Override
	public void handleItem() throws IOException {
		new ResponseOK(exchange).send(tasksToJSON());
	}
}
