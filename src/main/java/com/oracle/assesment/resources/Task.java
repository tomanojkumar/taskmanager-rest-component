package com.oracle.assesment.resources;

//import jakarta.validation.constraints.NotEmpty;

import javax.validation.constraints.NotEmpty;

public class Task {
    private int id;
    @NotEmpty
    private String description;
    @NotEmpty
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Task() {
        super();
    }

    public Task(int id, String description, String date) {
        super();
        this.id = id;
        this.description = description;
        this.date = date;
    }
}