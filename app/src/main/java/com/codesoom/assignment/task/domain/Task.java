package com.codesoom.assignment.task.domain;

import com.codesoom.assignment.storage.ListStorageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Task extends ListStorageEntity {
    private Long id;

    private String title;

    @Builder
    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, title=%s}", id, title);
    }
}
