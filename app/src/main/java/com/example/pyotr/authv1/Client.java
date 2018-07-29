package com.example.pyotr.authv1;
public class Client {
    private String nume;
    private String prenume;
    private String pozitie;
    String clientId;

    public Client() {
    }

    public Client(String clientId,String nume, String prenume, String pozitie) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;
        this.clientId=clientId;
    }
    public Client(String nume, String prenume, String pozitie) {
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
