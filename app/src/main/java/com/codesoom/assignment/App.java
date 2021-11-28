package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
	private static final int PORT = 8380;

	public static void main(String[] args) {
		try {
			//Address 설정
			InetSocketAddress address = new InetSocketAddress(PORT);
			HttpServer httpServer = HttpServer.create(address,0);

			//Context Path & Handler 설정
			HttpHandler handler = new DemoHttpHandler();
			httpServer.createContext("/", handler);

			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
