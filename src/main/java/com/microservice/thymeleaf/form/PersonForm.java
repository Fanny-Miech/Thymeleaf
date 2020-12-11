package com.microservice.thymeleaf.form;

import java.util.UUID;

public class PersonForm {

    private String name;
    private UUID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() { return id; }
}
