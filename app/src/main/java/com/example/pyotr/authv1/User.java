package com.example.pyotr.authv1;

public class User {
    String email;
    String password;
    String companyName;
    int numberOfEmployes;
    String companyField;
    String userID;


    public User()
    {

    }

    public User(String userID,String email, String password, String companyName, int numberOfEmployes, String companyField) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.numberOfEmployes=numberOfEmployes;
        this.companyField=companyField;
        this.userID=userID;
    }

    public int getNumberOfEmployes() {
        return numberOfEmployes;
    }

    public String getUserID() {
        return userID;
    }
    public String getCompanyField() {
        return companyField;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCompanyName() {
        return companyName;
    }
}