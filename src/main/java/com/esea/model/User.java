package com.esea.model;

public class User {
    private String userName;
    private String password;
    private Token token;
    public User() {
        super();
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
