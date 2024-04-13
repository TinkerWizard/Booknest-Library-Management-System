package com.library.booknest.model;

import jakarta.persistence.Entity;

@Entity
public class Librarian extends User {
    private String staffId;

    private String department;

    // Additional properties, getters, setters, constructors
}