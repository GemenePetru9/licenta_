package com.example.pyotr.authv1;

public class User {
    private String email;
    private String password;
    private String userID;
    private String rol;

    public User()
    {

    }

    public User(String userID,String email, String password,String rol)  {
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.rol=rol;
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;


    }
    public User(String userID,String email, String password) {
        this.userID=userID;
        this.email = email;
        this.password = password;


    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
