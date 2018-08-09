package com.example.pyotr.authv1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Login_Emp extends Activity  {
    ArrayList<Client> mEmpDataSet = new ArrayList<>();
    DatabaseReference databaseEmp;
    private GridView gridViewWeek;
    private TextView textViewDay;
    private Calendar now;
   List<String> das=new ArrayList<String>();
    String json="";
    int count=0;
 // public String[] day={"off","off","off","off","off","off","off"};
    private String[] strDays = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thusday",
            "Friday",
            "Saturday"
    };
    private String[] strMonths = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_angajat);
        databaseEmp = FirebaseDatabase.getInstance().getReference();


      //  mEmpDataSet = prepareDataSet();

        gridViewWeek=(GridView)findViewById(R.id.gridViewWeek) ;
        textViewDay=(TextView)findViewById(R.id.textViewSapt) ;
       // showDate();

       // DayAdapter dayAdapter= new DayAdapter(getApplicationContext(), mEmpDataSet);
       // gridViewWeek.setAdapter(dayAdapter);




        // Populate a List from Array elements


        // Create a new ArrayAdapter

      prepareDataSet();
        final ArrayAdapter<String> gridViewArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1, das);
        gridViewWeek.setAdapter(gridViewArrayAdapter);


    }

    public void decodeDate(String json) throws JSONException {


        // Creating a JSONObject from a String
        JSONObject jsonObject  = new JSONObject(json);

// Creating a sub-JSONObject from another JSONObject

// Getting the value of a attribute in a JSONObject
      //  String luni = jsonObject.getString("Monday");
       // String marti = jsonObject.getString("Tuesday");
       // String miercuri = jsonObject.getString("Wednesday");
      //  String joi = jsonObject.getString("Thusday");
        String vineri = jsonObject.getString("Friday");
        String sambata = jsonObject.getString("Saturday");
       // String duminca = jsonObject.getString("Sunday");
      // String luni = nodeRoot.getString("Monday");
        System.out.println("Orar: "+"friday:"+vineri+"saturday"+sambata);
    }


    private void prepareDataSet() {
        databaseEmp.child("client").child("-LJLQkTSuI0rSI-k6wtT").child("day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // GenericTypeIndicator<Map<String, Client>> t = new GenericTypeIndicator<Map<String, Client>>() {};
                //Map<String, Client> map = dataSnapshot.getValue(t);
                Object object = dataSnapshot.getValue(Object.class);
                json = new Gson().toJson(object);

                System.out.println("LOGIN date:" + json);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    // Getting the value of a attribute in a JSONObject
                    das.add(jsonObject.getString("Monday"));
                    das.add( jsonObject.getString("Tuesday"));
                    das.add(jsonObject.getString("Wednesday"));
                    das.add(jsonObject.getString("Thusday"));
                    das.add(jsonObject.getString("Friday"));
                    das.add( jsonObject.getString("Saturday"));
                    das.add(jsonObject.getString("Sunday"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }


        });
    }

    public void showDate()
    {
        now = Calendar.getInstance();
        //textViewDay.setText(strDays[now.get(Calendar.DAY_OF_WEEK)]+"-"+strDays[now.get(Calendar.DAY_OF_WEEK) +5] + ", " + strMonths[now.get(Calendar.MONTH)] + ", " + now.get(Calendar.DATE) + ", " + now.get(Calendar.YEAR));

    }

}
