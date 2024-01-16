package com.example.finalproject;

public class Customer {
    public String email;
    public String firstName;
    public String lastName;
    public String passwordHashed;
    public String phoneNumber;
    public String country;
    public String city;
    public String gender;
    public int isAdmin;

    // Constructors, getters, and setters

    // Example constructor:
    public Customer(String email, String firstName, String lastName, String passwordHashed,
                    String phoneNumber, String country, String city, String gender, int isAdmin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHashed = passwordHashed;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.city = city;
        this.gender = gender;
        this.isAdmin = isAdmin;
    }
}
