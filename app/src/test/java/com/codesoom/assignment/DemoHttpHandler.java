package com.codesoom.assignment;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DemoHttpHandler implements HttpHandler{
	List<Map<String, Object>> tasks = new ArrayList<>();
	
	@Override
	public void handle(HttpExchange exchange) throws IOException{
		
		String method = exchange.getRequestMethod();
		System.out.println(method);
		String content = "helo";
		
		//exchange.getRequestHeaders().get
		URI uri = exchange.getRequestURI();
		System.out.println(uri.getQuery());
		String path = uri.getPath();
		
		if("GET".equals(method)) {
			if("/tasks".equals(path)) {
				System.out.println(tasks);
				
			}
		}else if("POST".equals(method)) {
			if("/tasks".equals(path)) {
				exchange.getRequestBody();
			}
		}else if("PUT".equals(method)) {
			
		}else if("DELETE".equals(method)) {
			String[] aa = uri.getQuery().split("=");
			for(Map<String, Object>task : tasks) {
				if("".equals((String)task.get("id"))) {
					
				}
			}
		}
		else {
			
		}
		
		exchange.sendResponseHeaders(200,  content.getBytes().length);
		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
		
	}
}
