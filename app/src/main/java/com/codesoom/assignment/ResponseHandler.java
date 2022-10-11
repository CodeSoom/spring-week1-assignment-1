package com.codesoom.assignment;

import java.io.IOException;
import java.io.OutputStream;

import com.codesoom.assignment.methodController.DeleteController;
import com.codesoom.assignment.methodController.GetController;
import com.codesoom.assignment.methodController.PatchController;
import com.codesoom.assignment.methodController.PostController;
import com.codesoom.assignment.methodController.PutController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * RootHandler: http 요청 메서드에 따라 처리를 분류한다
 */
public class ResponseHandler implements HttpHandler {

    GetController getController = new GetController();
    PostController postController = new PostController();
    PatchController patchController = new PatchController();
    PutController putController = new PutController();
    DeleteController deleteController = new DeleteController();

    @Override

    public void handle(HttpExchange exchange) throws IOException {

        // Initialize Response Body
        OutputStream responseBody = exchange.getResponseBody();

        // http method에 따라 controller를 분기한다
        switch (exchange.getRequestMethod()) {
            case "get":
                getController.handler(exchange, responseBody);
                break;
            case "post":
                postController.handler(exchange, responseBody);
                break;
            case "put":
                putController.handler(exchange, responseBody);
                break;
            case "patch":
                patchController.handler(exchange, responseBody);
                break;
            case "delete":
                deleteController.handler(exchange, responseBody);
                break;

        }
    }
}
