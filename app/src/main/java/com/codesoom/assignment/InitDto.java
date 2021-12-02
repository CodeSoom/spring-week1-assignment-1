package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

//따로 DB를 호출 할수 있게 생성 했습니다.
public class InitDto {

    public void InitDb() {
        Task task = createTask(1L, "과제 제출하기");
    }

    private Task createTask(long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);

        return task;
    }

}
