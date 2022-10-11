package com.codesoom.assignment;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * HttpServerManager: 서버의 생성과 동작을 관리한다
 */
public class HttpServerManager {


    private HttpServer restApiServer = null;
    private final int DEFAULT_BACKLOG = 0;

    public HttpServerManager(String host, int port) throws IOException {
        createServer(host, port);
    }

    private void createServer(String host, int port) throws IOException {
        this.restApiServer = HttpServer.create(new InetSocketAddress(host, port), DEFAULT_BACKLOG);
        restApiServer.createContext("/", new ResponseHandler());
    }

    public void start() {
        restApiServer.start();
    }

    public void stop(int delay) {
        restApiServer.stop(delay);
    }
}
