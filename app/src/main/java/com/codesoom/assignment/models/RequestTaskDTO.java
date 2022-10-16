package com.codesoom.assignment.models;

public class RequestTaskDTO {

    public static class Create {
        private Long id;
        private String title;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Task toEntity() {
            Task task = new Task();
            task.setTitle(title);
            return task;
        }
    }

    public static class Update {
        private Long id;
        private String title;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Task toEntity() {
            Task task = new Task();
            task.setId(id);
            task.setTitle(title);
            return task;
        }
    }
}
