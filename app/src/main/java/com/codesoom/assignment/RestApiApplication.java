package com.codesoom.assignment;

import java.io.IOException;

public class RestApiApplication {

    public static void run() {

        HttpServerManager httpServerManager = null;
        ResponseHandler responseHandler = new ResponseHandler();

        try {
            httpServerManager = new HttpServerManager("localhost", 8000, responseHandler); // 서버를 생성한다.
            httpServerManager.start(); // 서버를 시작한다

            Runtime.getRuntime().addShutdownHook(new Thread() {
            });

            System.out.print("Please press 'Enter' to stop the server.");
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            httpServerManager.stop(0);
        }
    }
}

