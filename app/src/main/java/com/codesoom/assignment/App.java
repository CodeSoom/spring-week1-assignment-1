package com.codesoom.assignment;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
	public String getGreeting() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		System.out.println(new App().getGreeting());

		try {
			InetSocketAddress address = new InetSocketAddress(8080);
			HttpServer httpServer = HttpServer.create(address, 0);
			HttpHandler handler = new DemoHttpHandler();
			httpServer.createContext("/", handler);
			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
