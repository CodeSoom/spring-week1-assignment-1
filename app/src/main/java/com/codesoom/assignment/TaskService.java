package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    ObjectMapper objectMapper = new ObjectMapper();
    public List<Task> tasks = new ArrayList<>();
    private Long count = 1L;

    public String getTasks({
    }

    public String getTaskById(int id){
    }

    public String createTask(HttpExchange httpExchange){

    }

    public String updateTask(HttpExchange httpExchange){

    }

    public String deleteTask(HttpExchange httpExchange){

    }

}
