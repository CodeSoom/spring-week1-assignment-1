package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Map<String, Object>> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        String path = uri.getPath();
        String queryString = uri.getQuery();
        String content = "Nothing 404";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        System.out.println("uri : " +method + "|" + path + "?" + queryString);
        System.out.println("body : " + body);

        String[] temp = path.split("/");
        String target = "";
        String id = "";
        if(temp.length >= 2) target = temp[1];
        if(temp.length >= 3) id = temp[2];

        if("tasks".equals(target)){
            if("GET".equals(method)){
                content = objectMapper.writeValueAsString(tasks);
            }else if("POST".equals(method)){
                String title = body.split("=")[1];
                int newId = 0;
                if(tasks.size() != 0){
                    sortArray();
                    newId = Integer.parseInt(tasks.get(0).get("id").toString())+1;
                }

                Map<String, Object> task = new HashMap<>();
                task.put("id", newId);
                task.put("title", title);
                tasks.add(task);
                content = objectMapper.writeValueAsString(tasks);
            }else if("PUT".equals(method)){
                String title = body.split("=")[1];
                for(int i=0; i<tasks.size(); i++){
                    if(id.equals(tasks.get(i).get("id").toString())){
                        tasks.get(i).put("title", title);
                    }
                }
                content = objectMapper.writeValueAsString(tasks);
            }else if("DELETE".equals(method)){
                for(int i=0; i<tasks.size(); i++){
                    if(id.equals(tasks.get(i).get("id").toString())){
                        tasks.remove(i);
                    }
                }
                content = objectMapper.writeValueAsString(tasks);
            }
        }

        //Client가 요청했으니 뭐라도 Response를 전달해야함. 정상상태 200으로.
        exchange.sendResponseHeaders(200, content.getBytes().length); // 영어, 한글 byte길이가 다르므로 byte[]로 넘김.
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush(); //버퍼를 모두 출력하고 비우는 역할.
        outputStream.close();
    }

    public void sortArray(){
        Collections.sort(tasks, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer id1 = (Integer) o1.get("id");
                Integer id2 = (Integer) o2.get("id");
                return id2.compareTo(id1);
            }
        });
    }
}
