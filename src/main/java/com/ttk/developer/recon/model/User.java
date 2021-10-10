package com.ttk.developer.recon.model;

public class User{
    private long id;

    private String name;

    private String email;

    private String countryCode;

    public User() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}