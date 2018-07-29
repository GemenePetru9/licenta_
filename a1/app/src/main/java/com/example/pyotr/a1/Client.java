package com.example.pyotr.a1;

public class Client {
    private String nume;
    private String prenume;
    private String pozitie;

    public Client() {
    }

    public Client(String nume, String prenume, String pozitie) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;
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
}
