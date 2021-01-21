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

    /* Http Status Code 를 정의한 Enum */
    public enum HttpStatusCode {
        OK(200), CREATED(201), BAD_REQUEST(400), NOT_FOUND(404), METHOD_NOT_ALLOWED(405);

        private final int statusCode;

        HttpStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        public int getCode() {
            return this.statusCode;
        }
    }

    /* 사용자에게 전달할 Message 를 정의한 Enum */
    public enum ResultMessage {
        OK("200 OK"), CREATED("201 CREATED"), BAD_REQUEST("400 BAD_REQUEST"), NOT_FOUND("404 NOT_FOUND"), METHOD_NOT_ALLOWED("405 METHOD_NOT_ALLOWED");

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

        HttpHandler roothandler = new RootHttpHandler(); // "/" 로 들어왔을 경우 실행 될 Handler
        HttpHandler taskHandler = new TaskHttpHandler(); // "/tasks"로 들어왔을 경우 실행 될 Handler

        httpServer.createContext("/", roothandler);
        httpServer.createContext("/tasks", taskHandler);

        httpServer.start();
    }
}
