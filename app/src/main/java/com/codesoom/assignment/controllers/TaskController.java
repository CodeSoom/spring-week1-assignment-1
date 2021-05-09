package com.codesoom.assignment.controllers;
// 1. READ Collection - GET / tasks => 완료
// 2. READ Item - GET / tasks/{id}
// 3. CREATE POST/tasks => 완
// 4. UPDATE PUT/PATCH/tasks/{id}
// 5. DELETE DELETE/tasks/{id}

import com.codesoom.assignment.models.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    // @RequestMapping(path = "", method = RequestMethod.GET) // 이거 대신 @GetMapping 으로 써준다.
    @GetMapping
    public List<Task> list() {
        return tasks;
    }

    @GetMapping(path="/tasks/")
    public Task getTask(@RequestParam("id") int taskId) {
        System.out.println(taskId);
        return tasks.get(taskId);
    }

    // @RequestMapping(path = "", method = RequestMethod.POST) // 대신 @PostMapping
    @PostMapping
    public Task create(@RequestBody Task task) {

        task.setId(generateId());
        tasks.add(task);
        return task;
    }

    private Long generateId(){
        newId += 1;
        return newId;
    }


}
