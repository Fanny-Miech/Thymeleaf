package com.microservice.thymeleaf.model;

import java.util.UUID;

public class Person {

    private String name;
    private UUID id;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public Person(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }
}
