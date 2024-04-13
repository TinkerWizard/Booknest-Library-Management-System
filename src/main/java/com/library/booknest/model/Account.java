package com.library.booknest.model;

import jakarta.persistence.Entity;

@Entity
public class Account extends User {
    private String accountNumber;

    private double balance;

    // Additional properties, getters, setters, constructors
}