package com.codesoom.assignment;

import com.codesoom.assignment.handlers.RootHttpHandler;
import com.codesoom.assignment.handlers.TaskHttpHandler;
import com.google.common.primitives.Bytes;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static final int PORT = 8000;
    public static final int BACKLOG = 0;

    /** Http Status Code 를 정의합니다. */
    public enum HttpStatusCode {
        OK(200), CREATED(201), NO_CONTENT(204), BAD_REQUEST(400), NOT_FOUND(404), METHOD_NOT_ALLOWED(405);

        private final int statusCode;

        HttpStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        public int getCode() {
            return this.statusCode;
        }
    }

    /** 사용자에게 전달할 Message 를 정의합니다. */
    public enum ResultMessage {
        OK("200 OK"), CREATED("201 Created"), BAD_REQUEST("400 Bad Request"), NOT_FOUND("404 Not Found"), METHOD_NOT_ALLOWED("405 Method Not Allowed");

        private String resultMessage;

        ResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }

        public String getMessage() {
            return this.resultMessage;
        }

    }

    public String getGreeting() {
        return "Hello World!";
    }

    /* Entry Point */
    public static void main(String[] args) throws IOException {

        // Generate Java Server
        InetSocketAddress address = new InetSocketAddress(PORT);
        HttpServer httpServer = HttpServer.create(address, BACKLOG);

        httpServer.createContext("/", new RootHttpHandler());
        httpServer.createContext("/tasks", new TaskHttpHandler());

        httpServer.start();
    }
}
