package com.library.booknest.model;

import jakarta.persistence.Entity;

@Entity
public class Student extends User {
    private String studentId;

    private String department;

    // Additional properties, getters, setters, constructors
}