package com.codesoom.assignment.storage;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ListStorageEntity {

    @Setter
    private Long id;

    public ListStorageEntity(Long id) {
        this.id = id;
    }
}
