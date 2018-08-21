package com.example.pyotr.authv1;

public class Managar extends User {
    private String companyName;
    int numberOfEmployes;
    private String companyField;
    private String role;
    private Boolean status;
    private Boolean setSchedule;
    private String sapt1;

    public Managar()
    {

    }
    public Managar(String userID, String email, String password, String companyName, int numberOfEmployes, String companyField, Boolean status,Boolean setSchedule,String sapt1) {
        super(userID,email, password);
        this.companyName = companyName;
        this.numberOfEmployes = numberOfEmployes;
        this.companyField = companyField;
        this.role = role;
        this.status=status;
        this.setSchedule=setSchedule;
        this.sapt1=sapt1;
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

    public Boolean getSetSchedule() {
        return setSchedule;
    }

    public void setSetSchedule(Boolean setSchedule) {
        this.setSchedule = setSchedule;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSapt1() {
        return sapt1;
    }

    public void setSapt1(String sapt1) {
        this.sapt1 = sapt1;
    }
}
