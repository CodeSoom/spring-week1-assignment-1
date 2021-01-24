package com.codesoom.assignment;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            MyServer myServer = new MyServer();
            myServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
