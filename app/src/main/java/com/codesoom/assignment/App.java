package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
	private int port = 8380;
	public void httpServer(){
		try {
			//Address 설정
			InetSocketAddress address = new InetSocketAddress(port);
			HttpServer httpServer = HttpServer.create(address,0);

			//Context Path & Handler 설정
			HttpHandler handler = new DemoHttpHandler();
			httpServer.createContext("/", handler);

			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			//Address 설정
			InetSocketAddress address = new InetSocketAddress(8380);
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
