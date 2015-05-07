package com.example.matthew.constellate;

public class User {

    private String email;
    private String token;

    public User() {
        this.email = "";
        this.token = "";
    }

    public User(String email) {
        this.email = email;
        this.token = "";
    }

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return this.email; }

    public void setToken(String token) { this.token = token; }
    public String getToken() { return this.token; }
}
