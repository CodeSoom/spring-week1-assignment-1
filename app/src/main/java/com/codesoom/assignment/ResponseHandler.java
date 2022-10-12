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

    private final GetController getController = new GetController();
    private final PostController postController = new PostController();
    private final PatchController patchController = new PatchController();
    private final PutController putController = new PutController();
    private final DeleteController deleteController = new DeleteController();

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
