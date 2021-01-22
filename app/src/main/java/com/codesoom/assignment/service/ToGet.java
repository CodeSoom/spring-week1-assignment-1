package com.codesoom.assignment.service;

import com.codesoom.assignment.DemoHttpHandler;
import com.codesoom.assignment.Handle;
import com.codesoom.assignment.function.FinalLibrary;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class ToGet implements Handle {

    @Override
    public void check(String[] path, String body, HttpExchange exchange) throws IOException {

        if (path.length == 0) {
            FinalLibrary.responseOutput(FinalLibrary.OK, "Hello World", exchange);
            return;
        }

        if (!path[1].equals(FinalLibrary.OK_PATH)) {
            FinalLibrary.responseOutput(FinalLibrary.NOT_FOUND, "GET Path Error", exchange);
            return;
        }

        if (path.length >= 3) {
            int getNumber = Integer.parseInt(path[2]);
            if (!(FinalLibrary.tasks.size() <= getNumber -1)) {
                FinalLibrary.responseOutput(FinalLibrary.OK, FinalLibrary.tasks.get(getNumber -1).toString(), exchange);
                return;
            }
            FinalLibrary.responseOutput(FinalLibrary.NOT_FOUND, "해당 ID는 존재하지 않습니다.", exchange);
            return;
        }
        FinalLibrary.responseOutput(FinalLibrary.OK, FinalLibrary.tasksToJSON(), exchange);
    }
}
