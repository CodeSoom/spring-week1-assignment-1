package com.codesoom.assignment.method;

import java.io.IOException;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseNoContent;
import com.sun.net.httpserver.HttpExchange;

public class DeleteTask extends DoTask {
	public DeleteTask(HttpExchange exchange) {
		super(exchange);
	}

	@Override
	public void handleItem() throws IOException {
		Long id = Long.valueOf(exchange.getRequestURI().getPath().substring("/tasks/".length()));
		Task task = tasks.stream()
			.filter(t -> t.getId().equals(id))
			.findAny()
			.orElseThrow(IllegalArgumentException::new);
		tasks.remove(task);
		String content = tasksToJSON();
		new ResponseNoContent(exchange).send(content);
	}
}
