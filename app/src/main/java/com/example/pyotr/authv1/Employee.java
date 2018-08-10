package com.example.pyotr.authv1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Employee extends User {
    private String nume;
    private String prenume;
    private String manager;
    private String pozitie;
    private String clientId;
    private String shift;
    //private String[] day={"off","off","off","off","off","off","off"};
   // private List<String> day= Arrays.asList("off","off","off","off","off","off","off");


    public Employee()
    {

    }
    public Employee(String clientId,String email, String password,String nume, String prenume, String manager ) {
        super(email, password);
        this.clientId = clientId;
        this.nume = nume;
        this.prenume = prenume;
        this.manager = manager;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getClientId() {
        return clientId;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getPozitie() {
        return pozitie;
    }
    public String toString() {
        return nume+" "+prenume+" "+pozitie;
    }


    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setPozitie(String pozitie) {
        this.pozitie = pozitie;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


}
