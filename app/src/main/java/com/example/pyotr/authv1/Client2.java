package com.example.pyotr.authv1;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Client2 {
    private String nume;
    private String prenume;
    private String pozitie;
    String clientId;
    String shift;
    //Map<String,String> shift_per_day;
    public Client2() {

    }

    public Client2(String clientId,String nume, String prenume, String pozitie) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;
        this.clientId=clientId;
    }

    public Client2(String nume, String prenume, String pozitie) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;

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
