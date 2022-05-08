package com.codesoom.assignment.method;

import java.io.IOException;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseOK;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

public class GetTask extends DoTask {

	public GetTask(HttpExchange exchange) {
		super(exchange);
	}

	@Override
	public void handleItem() throws IOException {
		String content = taskToJSON(getIdFromPath(exchange));
		new ResponseOK(exchange).send(content);
	}

	@Override
	protected Task contentToTask(String content) throws JsonProcessingException {
		return null;
	}
}
