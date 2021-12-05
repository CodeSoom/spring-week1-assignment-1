package com.codesoom.assignment.task.domain;

import com.codesoom.assignment.storage.ListStorageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class Task extends ListStorageEntity {
    private String title;

    @Builder
    public Task(Long id, String title) {
        super(id);
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, title=%s}", getId(), getTitle());
    }
}
