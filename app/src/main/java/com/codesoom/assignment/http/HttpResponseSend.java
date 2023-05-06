package com.codesoom.assignment.http;

import java.io.IOException;

public interface HttpResponseSend {

	void send() throws IOException;

	void send(String responseBody) throws IOException;

}
