package com.example.pyotr.authv1;

public class Managar extends User {
    private String companyName;
    int numberOfEmployes;
    private String companyField;
    private String role;

    public Managar()
    {

    }
    public Managar(String userID,String email, String password,  String companyName, int numberOfEmployes, String companyField) {
        super(userID,email, password);
        this.companyName = companyName;
        this.numberOfEmployes = numberOfEmployes;
        this.companyField = companyField;
        this.role = role;
    }


    public int getNumberOfEmployes() {
        return numberOfEmployes;
    }
    public String getCompanyField() {
        return companyField;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setNumberOfEmployes(int numberOfEmployes) {
        this.numberOfEmployes = numberOfEmployes;
    }

    public void setCompanyField(String companyField) {
        this.companyField = companyField;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
