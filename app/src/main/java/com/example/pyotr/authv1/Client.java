package com.example.pyotr.authv1;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Client {
    private String nume;
    private String prenume;
    private String pozitie;
     private String clientId;
     private String shift;
     private String sapt;
    //private String[] day={"off","off","off","off","off","off","off"};
    private List<String> day=Arrays.asList("off","off","off","off","off","off","off");
    //Map<String,String> shift_per_day;
    public Client() {
    }

    public Client(String clientId,String nume, String prenume, String pozitie) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;
        this.clientId=clientId;
    }

    public Client(String nume, String prenume, String pozitie, String clientId, List<String> day,String sapt) {
        this.nume = nume;
        this.prenume = prenume;
        this.pozitie = pozitie;
        this.clientId = clientId;
        this.day = day;
        this.sapt=sapt;
    }

    /*public Client(Map<String, String> shift_per_day) {
            this.shift_per_day = shift_per_day;
        }
    */
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

    public String toStringShift() {
        String shift="";
        shift= nume+" "+prenume+" "+pozitie+" ";
        List<String> ore=new ArrayList<String>(day);
        for(int i=0;i<ore.size();i++)
        {
            shift+= ore.get(i) +" ";
        }

        return shift;
    }

    public String getSapt() {
        return sapt;
    }

    public void setSapt(String sapt) {
        this.sapt = sapt;
    }

    public String getShift() {
        return shift;
    }
    public void setShift(String shift) {
        this.shift = shift;
    }



    public List<String> getDay() {
        return new ArrayList<String>(day);
    }
    public void setDay(int position,String shift) {

        this.day.set(position,shift);
    }

    public void setDay(Map<String, Object> day) {

        for (Map.Entry<String, Object> entry : day.entrySet()) {

            System.out.println("entry key : "+entry.getKey());
            System.out.println("Object value :"+entry.getValue());
            if(entry.getKey().equals("Monday"))
            {
                this.day.set(0,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Tuesday"))
            {
                this.day.set(1,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Wednesday"))
            {
                this.day.set(2,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Thusday"))
            {
                this.day.set(3,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Friday"))
            {
                this.day.set(4,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Saturday"))
            {
                this.day.set(5,entry.getValue().toString());
            }
            else  if(entry.getKey().equals("Sunday"))
            {
                this.day.set(6,entry.getValue().toString());
            }

        }
    }


/*public Map<String, String> getShift_per_day() {
        return shift_per_day;
    }
    public String MapToString()
    {
        String date=" ";
        for (String name: shift_per_day.keySet()){

            String key =name.toString();
            String value = shift_per_day.get(name).toString();
            System.out.println(key + " " + value);
            date+=key+":"+value;


        }
        return date;
    }

    public void setShift_per_day(Map<String, String> shift_per_day) {
        this.shift_per_day = shift_per_day;
    }
    public  void setShift(String day,String shift)
    {

        shift_per_day.put(day, shift);
    }*/
}
